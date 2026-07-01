package com.rest.marketplace.infrastructure.messaging.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.marketplace.domain.exceptions.SqsEventException;
import com.rest.marketplace.domain.models.events.ProductCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductSqsEventPublisherTest {

    @Mock
    private SqsClient sqsClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductSqsEventPublisher publisher;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(publisher, "queueUrl", "https://sqs.us-east-1.amazonaws.com/123456789/test-queue");
    }

    @Test
    void debePublicarEventoEnSqsExitosamente() throws Exception {
        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .id(1L)
                .title("TV Samsung")
                .build();

        String payload = "{\"id\":1,\"title\":\"TV Samsung\"}";
        SendMessageResponse response = SendMessageResponse.builder()
                .messageId("msg-id-123")
                .build();

        when(objectMapper.writeValueAsString(event)).thenReturn(payload);
        when(sqsClient.sendMessage(any(SendMessageRequest.class))).thenReturn(response);

        publisher.publishProductCreated(event);

        verify(objectMapper).writeValueAsString(event);
        verify(sqsClient).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void debeLanzarSqsEventExceptionCuandoObjectMapperFalla() throws Exception {
        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .id(1L)
                .title("TV Samsung")
                .build();

        when(objectMapper.writeValueAsString(event))
                .thenThrow(new JsonProcessingException("error de serialización") {});

        assertThatThrownBy(() -> publisher.publishProductCreated(event))
                .isInstanceOf(SqsEventException.class)
                .hasMessageContaining("Error publicando evento en SQS");

        verify(sqsClient, never()).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void debeLanzarSqsEventExceptionCuandoSqsClientFalla() throws Exception {
        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .id(1L)
                .title("TV Samsung")
                .build();

        when(objectMapper.writeValueAsString(event)).thenReturn("{\"id\":1}");
        when(sqsClient.sendMessage(any(SendMessageRequest.class)))
                .thenThrow(new RuntimeException("SQS no disponible"));

        assertThatThrownBy(() -> publisher.publishProductCreated(event))
                .isInstanceOf(SqsEventException.class)
                .hasMessageContaining("Error publicando evento en SQS");
    }

    @Test
    void debeFallbackNoLanzarExcepcion() {
        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .id(1L)
                .title("TV Samsung")
                .build();

        Exception ex = new RuntimeException("Circuit breaker activado");

        publisher.fallbackPublishProductCreated(event, ex);

        verifyNoInteractions(sqsClient);
        verifyNoInteractions(objectMapper);
    }
}

