package com.rest.marketplace.infrastructure.messaging.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.marketplace.domain.models.events.ProductCreatedEvent;
import com.rest.marketplace.domain.models.outbox.OutboxEvent;
import com.rest.marketplace.domain.ports.outbox.OutboxPort;
import com.rest.marketplace.infrastructure.gateways.messaging.EventPublisherFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class OutboxProcessor {

	private final OutboxPort outboxPort;
	private final EventPublisherFactory eventPublisherFactory;
	private final ObjectMapper objectMapper;

	@Scheduled(fixedDelay = 30000) // corre cada 30 segundos
	public void processOutbox() {
		List<OutboxEvent> pendingEvents = outboxPort.findPending();

		if (pendingEvents.isEmpty()) return;

		log.info("📬 Procesando {} eventos pendientes", pendingEvents.size());

		var publisher = eventPublisherFactory.getPublisher(); // decide en runtime

		for (OutboxEvent outboxEvent : pendingEvents) {
			try {
				ProductCreatedEvent event = objectMapper
						.readValue(outboxEvent.getPayload(), ProductCreatedEvent.class);

				publisher.publishProductCreated(event);
				outboxPort.markAsProcessed(outboxEvent);
				log.info("✅ Evento procesado: {}", event.getTitle());

			} catch (Exception e) {
				log.error("❌ Error procesando evento {}: {}", outboxEvent.getId(), e.getMessage());
				outboxPort.markAsFailed(outboxEvent);
			}
		}
	}
}
