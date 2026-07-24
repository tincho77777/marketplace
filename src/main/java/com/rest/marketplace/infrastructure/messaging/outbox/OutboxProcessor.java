package com.rest.marketplace.infrastructure.messaging.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.marketplace.domain.models.events.ProductCreatedEvent;
import com.rest.marketplace.domain.models.outbox.OutboxEvent;
import com.rest.marketplace.domain.ports.out.ProductEventPort;
import com.rest.marketplace.domain.ports.outbox.OutboxPort;
import com.rest.marketplace.infrastructure.gateways.messaging.EventPublisherFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

		log.info("📬 Procesando {} eventos pendientes en paralelo", pendingEvents.size());

		var publisher = eventPublisherFactory.getPublisher(); // decide en runtime

		List<CompletableFuture<Void>> futures = pendingEvents.stream()
				.map(event -> CompletableFuture.runAsync(() -> processEvent(event, publisher)))
				.toList();

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
		log.info("✅ Todos los eventos procesados");
	}

	private void processEvent(OutboxEvent outboxEvent, ProductEventPort publisher) {
		try {
			log.info("[hilo: {}] 📤 Procesando evento: {}", Thread.currentThread().getName(), outboxEvent.getId());

			ProductCreatedEvent createdEvent = objectMapper.readValue(outboxEvent.getPayload(), ProductCreatedEvent.class);

			publisher.publishProductCreated(createdEvent);
			outboxPort.markAsProcessed(outboxEvent);

		} catch (Exception e){
			log.error("[hilo: {}] ❌ Error: {}", Thread.currentThread().getName(), e.getMessage());
			log.info("[hilo: {}] 🔄 Evento {} permanece en estado PENDING para reintento", Thread.currentThread().getName(), outboxEvent.getId());
		}
	}
}
