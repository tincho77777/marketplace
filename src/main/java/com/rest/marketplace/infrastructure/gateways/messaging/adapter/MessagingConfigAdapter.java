package com.rest.marketplace.infrastructure.gateways.messaging.adapter;

import com.rest.marketplace.domain.ports.messaging.MessagingConfigPort;
import com.rest.marketplace.infrastructure.gateways.messaging.entity.MessagingConfigEntity;
import com.rest.marketplace.infrastructure.gateways.messaging.repository.MessagingConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessagingConfigAdapter implements MessagingConfigPort {

	private final MessagingConfigRepository messagingConfigRepository;

	private static final String PARAMETRO_RABBIT = "RABBIT";

	@Override
	public String findActiveProvider() {
		return messagingConfigRepository.findActiveConfig()
				.map(MessagingConfigEntity::getProvider)
				.orElseGet(() -> {
					log.warn("No se encontró configuración de mensajería activa, usando RABBIT por defecto");
					return PARAMETRO_RABBIT;
				});
	}
}
