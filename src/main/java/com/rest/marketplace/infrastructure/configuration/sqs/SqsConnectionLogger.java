package com.rest.marketplace.infrastructure.configuration.sqs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsConnectionLogger {

    private final SqsClient sqsClient;

    @Value("${aws.sqs.endpoint}")
    private String endpoint;

    @Value("${aws.sqs.region}")
    private String region;

    @Value("${aws.sqs.queue-name}")
    private String queueName;

    @EventListener(ApplicationReadyEvent.class)
    public void checkSqsConnection() {
        try {
            // intenta obtener la URL de la cola
            var queueUrlRequest = GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build();
            getOrCreateQueue(queueUrlRequest);
        } catch (Exception e) {
            log.error("❌ Error conectando a SQS -> endpoint: {}, region: {} | Error: {}",
                    endpoint, region, e.getMessage());
        }
    }

    private void getOrCreateQueue(GetQueueUrlRequest queueUrlRequest) {
        try {
            var queueUrl = sqsClient.getQueueUrl(queueUrlRequest).queueUrl();
            log.info("✅ Queue '{}' encontrada -> URL: {}", queueName, queueUrl);
        } catch (QueueDoesNotExistException e) {
            // si no existe, la crea automáticamente
            log.warn("⚠️ Queue '{}' no encontrada, creándola...", queueName);

            var createRequest = CreateQueueRequest.builder()
                    .queueName(queueName)
                    .build();

            var queueUrl = sqsClient.createQueue(createRequest).queueUrl();
            log.info("✅ Queue '{}' creada exitosamente -> URL: {}", queueName, queueUrl);
        }
    }
}

