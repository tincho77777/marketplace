package com.rest.marketplace.infrastructure.gateways.messaging;

import com.rest.marketplace.domain.ports.messaging.MessagingConfigPort;
import com.rest.marketplace.domain.ports.out.ProductEventPort;
import com.rest.marketplace.infrastructure.messaging.rabbit.ProductRabbitEventPublisher;
import com.rest.marketplace.infrastructure.messaging.sqs.ProductSqsEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisherFactory {

	private final MessagingConfigPort messagingConfigPort;
	private final ProductRabbitEventPublisher rabbitPublisher;
	private final ProductSqsEventPublisher sqsPublisher;

	public ProductEventPort getPublisher() {
		var provider = messagingConfigPort.findActiveProvider();
		log.info("🔧 Usando provider de mensajería: {}", provider);

		return switch (provider) {
			case "SQS" -> sqsPublisher;
			default -> rabbitPublisher;
		};
	}
}
