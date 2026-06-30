package com.rest.marketplace.domain.models.outbox;

import com.rest.marketplace.domain.enums.outbox.OutboxStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OutboxEventTest {

    @Test
    void debeCrearOutboxEventConBuilder() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        OutboxEvent event = OutboxEvent.builder()
                .id(id)
                .eventType("PRODUCT_CREATED")
                .payload("{\"id\":1}")
                .status(OutboxStatus.PENDING)
                .createdAt(now)
                .processedAt(null)
                .build();

        assertThat(event.getId()).isEqualTo(id);
        assertThat(event.getEventType()).isEqualTo("PRODUCT_CREATED");
        assertThat(event.getPayload()).isEqualTo("{\"id\":1}");
        assertThat(event.getStatus()).isEqualTo(OutboxStatus.PENDING);
        assertThat(event.getCreatedAt()).isEqualTo(now);
        assertThat(event.getProcessedAt()).isNull();
    }

    @Test
    void debeCrearOutboxEventConConstructorVacio() {
        OutboxEvent event = new OutboxEvent();

        assertThat(event.getId()).isNull();
        assertThat(event.getEventType()).isNull();
        assertThat(event.getPayload()).isNull();
        assertThat(event.getStatus()).isNull();
    }

    @Test
    void debePermitirCambiarEstadoConSetter() {
        OutboxEvent event = OutboxEvent.builder()
                .status(OutboxStatus.PENDING)
                .build();

        event.setStatus(OutboxStatus.PROCESSED);
        event.setProcessedAt(LocalDateTime.now());

        assertThat(event.getStatus()).isEqualTo(OutboxStatus.PROCESSED);
        assertThat(event.getProcessedAt()).isNotNull();
    }

    @Test
    void debePermitirCambiarEstadoAFailed() {
        OutboxEvent event = OutboxEvent.builder()
                .status(OutboxStatus.PENDING)
                .build();

        event.setStatus(OutboxStatus.FAILED);

        assertThat(event.getStatus()).isEqualTo(OutboxStatus.FAILED);
    }
}

