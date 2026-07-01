package com.rest.marketplace.infrastructure.gateways.messaging;

import com.rest.marketplace.domain.ports.messaging.MessagingConfigPort;
import com.rest.marketplace.domain.ports.out.ProductEventPort;
import com.rest.marketplace.infrastructure.messaging.rabbit.ProductRabbitEventPublisher;
import com.rest.marketplace.infrastructure.messaging.sqs.ProductSqsEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventPublisherFactoryTest {

    @Mock
    private MessagingConfigPort messagingConfigPort;

    @Mock
    private ProductRabbitEventPublisher rabbitPublisher;

    @Mock
    private ProductSqsEventPublisher sqsPublisher;

    @InjectMocks
    private EventPublisherFactory eventPublisherFactory;

    @Test
    void debeRetornarSqsPublisherCuandoProviderEsSqs() {
        when(messagingConfigPort.findActiveProvider()).thenReturn("SQS");

        ProductEventPort publisher = eventPublisherFactory.getPublisher();

        assertThat(publisher).isSameAs(sqsPublisher);
    }

    @Test
    void debeRetornarRabbitPublisherCuandoProviderEsRabbit() {
        when(messagingConfigPort.findActiveProvider()).thenReturn("RABBIT");

        ProductEventPort publisher = eventPublisherFactory.getPublisher();

        assertThat(publisher).isSameAs(rabbitPublisher);
    }

    @Test
    void debeRetornarRabbitPublisherCuandoProviderEsDesconocido() {
        when(messagingConfigPort.findActiveProvider()).thenReturn("KAFKA");

        ProductEventPort publisher = eventPublisherFactory.getPublisher();

        assertThat(publisher).isSameAs(rabbitPublisher);
    }
}

