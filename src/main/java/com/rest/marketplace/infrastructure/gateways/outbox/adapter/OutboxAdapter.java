package com.rest.marketplace.infrastructure.gateways.outbox.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.marketplace.domain.enums.outbox.OutboxStatus;
import com.rest.marketplace.domain.exceptions.OutboxEventException;
import com.rest.marketplace.domain.models.events.ProductCreatedEvent;
import com.rest.marketplace.domain.models.outbox.OutboxEvent;
import com.rest.marketplace.domain.ports.outbox.OutboxPort;
import com.rest.marketplace.infrastructure.gateways.outbox.mapper.OutboxMapper;
import com.rest.marketplace.infrastructure.gateways.outbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxAdapter implements OutboxPort {

	private final OutboxRepository outboxRepository;
	private final ObjectMapper objectMapper;

	@Override
	public void saveProductCreatedEvent(ProductCreatedEvent event) {
		try {
			var outboxEvent = OutboxEvent.builder()
					.eventType("PRODUCT_CREATED")
					.payload(objectMapper.writeValueAsString(event))
					.status(OutboxStatus.PENDING)
					.createdAt(LocalDateTime.now())
					.build();

			outboxRepository.save(OutboxMapper.toEntity(outboxEvent));

		} catch (Exception e) {
			log.error("Error serializando evento outbox: {}", e.getMessage());
			throw new OutboxEventException("Error creando evento outbox", e);
		}
	}

	@Override
	public void save(OutboxEvent outboxEvent) {
		outboxRepository.save(OutboxMapper.toEntity(outboxEvent));
	}

	@Override
	public List<OutboxEvent> findPending() {
		return outboxRepository.findByStatus(OutboxStatus.PENDING)
				.stream()
				.map(OutboxMapper::toDomain)
				.toList();
	}

	@Override
	public void markAsProcessed(OutboxEvent outboxEvent) {
		outboxEvent.setStatus(OutboxStatus.PROCESSED);
		outboxEvent.setProcessedAt(LocalDateTime.now());
		outboxRepository.save(OutboxMapper.toEntity(outboxEvent));
	}

	@Override
	public void markAsFailed(OutboxEvent outboxEvent) {
		outboxEvent.setStatus(OutboxStatus.FAILED);
		outboxEvent.setProcessedAt(LocalDateTime.now());
		outboxRepository.save(OutboxMapper.toEntity(outboxEvent));
	}
}
