package com.rest.marketplace.infrastructure.messaging.rabbit;

import com.rest.marketplace.domain.models.events.ProductCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductRabbitEventPublisherTest {

	@Mock
	private RabbitTemplate rabbitTemplate;

	@InjectMocks
	private ProductRabbitEventPublisher publisher;

	@BeforeEach
	void init() {
		ReflectionTestUtils.setField(
				publisher,
				"exchange",
				"marketplace.exchange");

		ReflectionTestUtils.setField(
				publisher,
				"routingKey",
				"product.created");
	}

	@Test
	void debePublicarEventoEnRabbit() {

		ProductCreatedEvent event = ProductCreatedEvent.builder()
				.id(1L)
				.title("TV")
				.build();

		publisher.publishProductCreated(event);

		verify(rabbitTemplate).convertAndSend(
				"marketplace.exchange",
				"product.created",
				event
		);
	}

}