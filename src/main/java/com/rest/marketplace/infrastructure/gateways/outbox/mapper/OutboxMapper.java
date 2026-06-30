package com.rest.marketplace.infrastructure.gateways.outbox.mapper;

import com.rest.marketplace.domain.models.outbox.OutboxEvent;
import com.rest.marketplace.infrastructure.gateways.outbox.entity.OutboxEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OutboxMapper {

	public static OutboxEvent toDomain(OutboxEntity entity){
		return OutboxEvent.builder()
				.id(entity.getId())
				.eventType(entity.getEventType())
				.payload(entity.getPayload())
				.status(entity.getStatus())
				.createdAt(entity.getCreatedAt())
				.processedAt(entity.getProcessedAt())
				.build();
	}

	public static OutboxEntity toEntity(OutboxEvent event){
		return OutboxEntity.builder()
				.id(event.getId())
				.eventType(event.getEventType())
				.payload(event.getPayload())
				.status(event.getStatus())
				.createdAt(event.getCreatedAt())
				.processedAt(event.getProcessedAt())
				.build();
	}
}
