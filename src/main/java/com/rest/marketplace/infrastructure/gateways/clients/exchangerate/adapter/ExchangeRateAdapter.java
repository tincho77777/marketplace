package com.rest.marketplace.infrastructure.gateways.clients.exchangerate.adapter;

import com.rest.marketplace.domain.ports.exchangerate.ExchangeRatePort;
import com.rest.marketplace.infrastructure.gateways.clients.exchangerate.ExchangeRateClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateAdapter implements ExchangeRatePort {

	private static final String CACHE_NAME = "exchangeRate";
	private static final String CACHE_KEY = "ARS_USD";
	private static final BigDecimal FALLBACK_DEFAULT = new BigDecimal("0.00089");

	private final ExchangeRateClient exchangeRateClient;
	private final CacheManager cacheManager;

	@Value("${exchange-rate.api-key}")
	private String apiKey;

	//El proxy de @Cacheable intercepta la llamada antes de ejecutar el servicio. Si ya hay un valor guardado con la clave ARS_USD, devuelve ese valor directamente y el cuerpo de getUsdRate() jamás se ejecuta.
	@Override
	@Cacheable(value = CACHE_NAME, key = "'ARS_USD'")  // primero: si está en cache, ni llega al Circuit Breaker
	@CircuitBreaker(name = "exchangeRate", fallbackMethod = "fallbackGetUsdRate")  // segundo: evalúa si el circuito está abierto
	@Retry(name = "exchangeRate") // tercero: reintenta si falla
	public BigDecimal getUsdRate() {
		var responseApi = exchangeRateClient.getLatestRates(apiKey);
		var usdRate = BigDecimal.valueOf(responseApi.getConversionRates().get("USD"));
		log.info("💱 Tipo de cambio ARS/USD obtenido de API: {}", usdRate);
		return usdRate;
	}

	public BigDecimal fallbackGetUsdRate(Exception ex) {
		log.warn("⚠️ ExchangeRate-API no disponible. Causa: {}", ex.getMessage());

		var cache = cacheManager.getCache(CACHE_NAME);
		if(cache != null) {
			var cachedValue = cache.get(CACHE_KEY, BigDecimal.class);
			if(cachedValue != null) {
				log.info("💱 Usando último tipo de cambio cacheado: {}", cachedValue);
				return cachedValue;
			}
		}

		log.warn("⚠️ No hay valor cacheado disponible. Usando valor por defecto: {}", FALLBACK_DEFAULT);
		return FALLBACK_DEFAULT;
	}
}
