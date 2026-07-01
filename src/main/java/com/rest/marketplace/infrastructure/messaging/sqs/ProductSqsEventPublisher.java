package com.rest.marketplace.infrastructure.messaging.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.marketplace.domain.exceptions.SqsEventException;
import com.rest.marketplace.domain.models.events.ProductCreatedEvent;
import com.rest.marketplace.domain.ports.out.ProductEventPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductSqsEventPublisher implements ProductEventPort {

	private final SqsClient sqsClient;
	private final ObjectMapper objectMapper;

	@Value("${aws.sqs.queue-url}")
	private String queueUrl;

	@Override
	@CircuitBreaker(name = "sqs", fallbackMethod = "fallbackPublishProductCreated")
	@Retry(name = "sqs")
	public void publishProductCreated(ProductCreatedEvent event) {
		try {
			var payload = objectMapper.writeValueAsString(event);

			var request = SendMessageRequest.builder()
					.queueUrl(queueUrl)
					.messageBody(payload)
					.build();

			var response = sqsClient.sendMessage(request);

			log.info("✅ Evento publicado en SQS. MessageId: {}", response.messageId());

		} catch (Exception e) {
			log.error("❌ Error publicando en SQS: {}", e.getMessage());
			throw new SqsEventException("Error publicando evento en SQS", e);
		}
	}

	public void fallbackPublishProductCreated(ProductCreatedEvent event, Exception ex) {
		log.error("==========================================");
		log.error("❌ Circuit Breaker SQS ACTIVADO");
		log.error("📦 Evento no publicado: {}", event.getTitle());
		log.error("🔴 Causa: {}", ex.getMessage());
		log.error("==========================================");
	}

}
