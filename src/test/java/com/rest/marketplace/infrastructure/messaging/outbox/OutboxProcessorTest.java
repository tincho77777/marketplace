package com.rest.marketplace.infrastructure.messaging.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.marketplace.domain.enums.outbox.OutboxStatus;
import com.rest.marketplace.domain.models.events.ProductCreatedEvent;
import com.rest.marketplace.domain.models.outbox.OutboxEvent;
import com.rest.marketplace.domain.ports.out.ProductEventPort;
import com.rest.marketplace.domain.ports.outbox.OutboxPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxProcessorTest {

    @Mock
    private OutboxPort outboxPort;

    @Mock
    private ProductEventPort productEventPort;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OutboxProcessor outboxProcessor;

    @Test
    void debeProcessarEventosPendientesCorrectamente() throws Exception {
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .eventType("PRODUCT_CREATED")
                .payload("{\"id\":1,\"title\":\"TV Samsung\"}")
                .status(OutboxStatus.PENDING)
                .build();

        ProductCreatedEvent productEvent = ProductCreatedEvent.builder()
                .id(1L)
                .title("TV Samsung")
                .build();

        when(outboxPort.findPending()).thenReturn(List.of(outboxEvent));
        when(objectMapper.readValue(outboxEvent.getPayload(), ProductCreatedEvent.class))
                .thenReturn(productEvent);

        outboxProcessor.processOutbox();

        verify(productEventPort).publishProductCreated(productEvent);
        verify(outboxPort).markAsProcessed(outboxEvent);
        verify(outboxPort, never()).markAsFailed(any());
    }

    @Test
    void debeMarcarComoFailedCuandoOcurreExcepcion() throws Exception {
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .eventType("PRODUCT_CREATED")
                .payload("payload-invalido")
                .status(OutboxStatus.PENDING)
                .build();

        when(outboxPort.findPending()).thenReturn(List.of(outboxEvent));
        when(objectMapper.readValue(eq("payload-invalido"), eq(ProductCreatedEvent.class)))
                .thenThrow(new RuntimeException("error de deserialización"));

        outboxProcessor.processOutbox();

        verify(outboxPort).markAsFailed(outboxEvent);
        verify(outboxPort, never()).markAsProcessed(any());
        verify(productEventPort, never()).publishProductCreated(any());
    }

    @Test
    void debeNoHacerNadaCuandoNoHayEventosPendientes() {
        when(outboxPort.findPending()).thenReturn(List.of());

        outboxProcessor.processOutbox();

        verify(productEventPort, never()).publishProductCreated(any());
        verify(outboxPort, never()).markAsProcessed(any());
        verify(outboxPort, never()).markAsFailed(any());
    }

    @Test
    void debeProcesarMultiplesEventosPendientes() throws Exception {
        OutboxEvent event1 = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .payload("{\"id\":1}")
                .status(OutboxStatus.PENDING)
                .build();

        OutboxEvent event2 = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .payload("{\"id\":2}")
                .status(OutboxStatus.PENDING)
                .build();

        ProductCreatedEvent productEvent1 = ProductCreatedEvent.builder().id(1L).build();
        ProductCreatedEvent productEvent2 = ProductCreatedEvent.builder().id(2L).build();

        when(outboxPort.findPending()).thenReturn(List.of(event1, event2));
        when(objectMapper.readValue(event1.getPayload(), ProductCreatedEvent.class)).thenReturn(productEvent1);
        when(objectMapper.readValue(event2.getPayload(), ProductCreatedEvent.class)).thenReturn(productEvent2);

        outboxProcessor.processOutbox();

        verify(productEventPort).publishProductCreated(productEvent1);
        verify(productEventPort).publishProductCreated(productEvent2);
        verify(outboxPort).markAsProcessed(event1);
        verify(outboxPort).markAsProcessed(event2);
    }
}

