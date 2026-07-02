package com.rest.marketplace.infrastructure.configuration.vault;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class VaultConnectionLogger {

    // Rutas configuradas en spring.config.import (sin el prefijo vault://)
    private static final List<String> VAULT_PATHS = List.of(
            "secret/marketplace/database",
            "secret/marketplace/redis",
            "secret/marketplace/rabbitmq",
            "secret/marketplace/aws"
    );

    // Propiedades críticas que DEBEN venir de Vault
    private static final List<String> VAULT_EXPECTED_KEYS = List.of(
            "spring.datasource.url",
            "spring.datasource.username",
            "spring.data.redis.host",
            "spring.rabbitmq.host",
            "aws.sqs.endpoint"
    );

    private final ConfigurableEnvironment environment;

    @EventListener(ApplicationReadyEvent.class)
    public void checkVaultConnection() {
        List<EnumerablePropertySource<?>> vaultSources = new ArrayList<>();

        for (PropertySource<?> source : environment.getPropertySources()) {
            // Los property sources de Vault se llaman igual al path, e.g. "secret/marketplace/database"
            // y su clase contiene "Vault" en el nombre
            boolean isVaultByName = VAULT_PATHS.contains(source.getName());
            boolean isVaultByClass = source.getClass().getName().toLowerCase().contains("vault");

            if ((isVaultByName || isVaultByClass) && source instanceof EnumerablePropertySource<?> enumSource) {
                vaultSources.add(enumSource);
            }
        }

        if (!vaultSources.isEmpty()) {
            int total = vaultSources.stream().mapToInt(s -> s.getPropertyNames().length).sum();
            log.info("✅ Vault ACTIVO — {} secrets obtenidos desde {} rutas", total, vaultSources.size());
            checkExpectedSecrets();
        } else {
            log.warn("⚠️  Vault NO esta activo o no cargo ninguna property source.");
            log.warn("   Verifica: docker-compose up vault y que los secrets esten cargados.");
            log.warn("   URI configurada : {}", environment.getProperty("spring.cloud.vault.uri"));
            log.warn("   Token configurado: {}", environment.getProperty("spring.cloud.vault.token") != null ? "SI" : "NO");
        }
    }

    private void checkExpectedSecrets() {
        long missing = VAULT_EXPECTED_KEYS.stream()
                .filter(key -> {
                    String value = environment.getProperty(key);
                    return value == null || value.isBlank();
                })
                .count();

        if (missing == 0) {
            log.info("✅ Todos los secrets criticos fueron obtenidos correctamente");
        } else {
            log.warn("⚠️  {} secret(s) esperados no fueron encontrados. Revisa los paths en Vault.", missing);
        }
    }
}
