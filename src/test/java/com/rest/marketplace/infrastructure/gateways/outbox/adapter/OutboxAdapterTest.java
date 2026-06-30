package com.rest.marketplace.infrastructure.gateways.outbox.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.marketplace.domain.enums.outbox.OutboxStatus;
import com.rest.marketplace.domain.exceptions.OutboxEventException;
import com.rest.marketplace.domain.models.events.ProductCreatedEvent;
import com.rest.marketplace.domain.models.outbox.OutboxEvent;
import com.rest.marketplace.infrastructure.gateways.outbox.entity.OutboxEntity;
import com.rest.marketplace.infrastructure.gateways.outbox.repository.OutboxRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxAdapterTest {

    @Mock
    private OutboxRepository outboxRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OutboxAdapter outboxAdapter;

    @Test
    void debeGuardarEventoProductCreatedCorrectamente() throws Exception {
        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .id(1L)
                .title("TV Samsung")
                .build();

        when(objectMapper.writeValueAsString(event)).thenReturn("{\"id\":1,\"title\":\"TV Samsung\"}");
        when(outboxRepository.save(any(OutboxEntity.class))).thenReturn(new OutboxEntity());

        outboxAdapter.saveProductCreatedEvent(event);

        ArgumentCaptor<OutboxEntity> captor = ArgumentCaptor.forClass(OutboxEntity.class);
        verify(outboxRepository).save(captor.capture());

        OutboxEntity saved = captor.getValue();
        assertThat(saved.getEventType()).isEqualTo("PRODUCT_CREATED");
        assertThat(saved.getPayload()).isEqualTo("{\"id\":1,\"title\":\"TV Samsung\"}");
        assertThat(saved.getStatus()).isEqualTo(OutboxStatus.PENDING);
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void debeLanzarOutboxEventExceptionCuandoFallaLaSerializacion() throws Exception {
        ProductCreatedEvent event = ProductCreatedEvent.builder().id(1L).build();

        when(objectMapper.writeValueAsString(event)).thenThrow(new RuntimeException("error de serialización"));

        assertThrows(OutboxEventException.class, () -> outboxAdapter.saveProductCreatedEvent(event));

        verify(outboxRepository, never()).save(any());
    }

    @Test
    void debeGuardarOutboxEventDirectamente() {
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .eventType("PRODUCT_CREATED")
                .payload("{}")
                .status(OutboxStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(outboxRepository.save(any(OutboxEntity.class))).thenReturn(new OutboxEntity());

        outboxAdapter.save(outboxEvent);

        verify(outboxRepository).save(any(OutboxEntity.class));
    }

    @Test
    void debeRetornarEventosPendientes() {
        UUID id = UUID.randomUUID();
        OutboxEntity entity = OutboxEntity.builder()
                .id(id)
                .eventType("PRODUCT_CREATED")
                .payload("{}")
                .status(OutboxStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(outboxRepository.findByStatus(OutboxStatus.PENDING)).thenReturn(List.of(entity));

        List<OutboxEvent> result = outboxAdapter.findPending();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(id);
        assertThat(result.getFirst().getStatus()).isEqualTo(OutboxStatus.PENDING);

        verify(outboxRepository).findByStatus(OutboxStatus.PENDING);
    }

    @Test
    void debeRetornarListaVaciaCuandoNoHayPendientes() {
        when(outboxRepository.findByStatus(OutboxStatus.PENDING)).thenReturn(List.of());

        List<OutboxEvent> result = outboxAdapter.findPending();

        assertThat(result).isEmpty();
    }

    @Test
    void debeMarcarEventoComoProcessed() {
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .status(OutboxStatus.PENDING)
                .build();

        when(outboxRepository.save(any(OutboxEntity.class))).thenReturn(new OutboxEntity());

        outboxAdapter.markAsProcessed(outboxEvent);

        assertThat(outboxEvent.getStatus()).isEqualTo(OutboxStatus.PROCESSED);
        assertThat(outboxEvent.getProcessedAt()).isNotNull();
        verify(outboxRepository).save(any(OutboxEntity.class));
    }

    @Test
    void debeMarcarEventoComoFailed() {
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .status(OutboxStatus.PENDING)
                .build();

        when(outboxRepository.save(any(OutboxEntity.class))).thenReturn(new OutboxEntity());

        outboxAdapter.markAsFailed(outboxEvent);

        assertThat(outboxEvent.getStatus()).isEqualTo(OutboxStatus.FAILED);
        assertThat(outboxEvent.getProcessedAt()).isNotNull();
        verify(outboxRepository).save(any(OutboxEntity.class));
    }
}

