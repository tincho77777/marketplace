package com.rest.marketplace.infrastructure.gateways.outbox.mapper;

import com.rest.marketplace.domain.enums.outbox.OutboxStatus;
import com.rest.marketplace.domain.models.outbox.OutboxEvent;
import com.rest.marketplace.infrastructure.gateways.outbox.entity.OutboxEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OutboxMapperTest {

    @Test
    void debeMapearCorrectamenteDeEntityADomain() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        OutboxEntity entity = OutboxEntity.builder()
                .id(id)
                .eventType("PRODUCT_CREATED")
                .payload("{\"id\":1}")
                .status(OutboxStatus.PENDING)
                .createdAt(now)
                .processedAt(null)
                .build();

        OutboxEvent result = OutboxMapper.toDomain(entity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(entity.getId());
        assertThat(result.getEventType()).isEqualTo(entity.getEventType());
        assertThat(result.getPayload()).isEqualTo(entity.getPayload());
        assertThat(result.getStatus()).isEqualTo(entity.getStatus());
        assertThat(result.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(result.getProcessedAt()).isNull();
    }

    @Test
    void debeMapearCorrectamenteDeDomainAEntity() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime processedAt = now.plusMinutes(1);

        OutboxEvent event = OutboxEvent.builder()
                .id(id)
                .eventType("PRODUCT_CREATED")
                .payload("{\"id\":1}")
                .status(OutboxStatus.PROCESSED)
                .createdAt(now)
                .processedAt(processedAt)
                .build();

        OutboxEntity result = OutboxMapper.toEntity(event);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(event.getId());
        assertThat(result.getEventType()).isEqualTo(event.getEventType());
        assertThat(result.getPayload()).isEqualTo(event.getPayload());
        assertThat(result.getStatus()).isEqualTo(event.getStatus());
        assertThat(result.getCreatedAt()).isEqualTo(event.getCreatedAt());
        assertThat(result.getProcessedAt()).isEqualTo(event.getProcessedAt());
    }

    @Test
    void debeMapearEstadoFailedCorrectamente() {
        OutboxEntity entity = OutboxEntity.builder()
                .id(UUID.randomUUID())
                .eventType("PRODUCT_CREATED")
                .payload("{\"id\":2}")
                .status(OutboxStatus.FAILED)
                .createdAt(LocalDateTime.now())
                .build();

        OutboxEvent result = OutboxMapper.toDomain(entity);

        assertThat(result.getStatus()).isEqualTo(OutboxStatus.FAILED);
    }
}

