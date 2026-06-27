package com.rest.marketplace.infrastructure.messaging.rabbit;

import com.rest.marketplace.domain.models.events.ProductCreatedEvent;
import com.rest.marketplace.domain.ports.out.ProductEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventPublisher implements ProductEventPort {

	private final RabbitTemplate rabbitTemplate;

	@Value("${rabbitmq.exchange}")
	private String exchange;

	@Value("${rabbitmq.routing-key}")
	private String routingKey;

	public void publishProductCreated(ProductCreatedEvent event){
		log.info("Publicando evento de producto creado: {}", event.getTitle());
		rabbitTemplate.convertAndSend(exchange, routingKey, event);
		log.info("Evento publicado exitosamente en exchange: {}", exchange);
	}
}
