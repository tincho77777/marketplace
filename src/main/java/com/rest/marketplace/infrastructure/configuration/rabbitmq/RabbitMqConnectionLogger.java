package com.rest.marketplace.infrastructure.configuration.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMqConnectionLogger {

    private final ConnectionFactory connectionFactory;

    @EventListener(ApplicationReadyEvent.class)
    public void checkRabbitMqConnection() {
        var host = connectionFactory.getHost();
        var port = connectionFactory.getPort();
        try {
            var connection = connectionFactory.createConnection();
            if (connection.isOpen()) {
                log.info("✅ Conexión a RabbitMQ EXITOSA -> host: {}, port: {}", host, port);
            } else {
                log.warn("⚠️  La conexión a RabbitMQ se creó pero NO está abierta -> host: {}, port: {}", host, port);
            }
            connection.close();
        } catch (Exception e) {
            log.error("❌ No se pudo conectar a RabbitMQ -> host: {}, port: {} | Error: {}", host, port, e.getMessage());
        }
    }
}

