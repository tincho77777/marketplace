package com.rest.marketplace.infrastructure.configuration.rabbitmq;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

	@Value("${rabbitmq.exchange}")
	private String exchange;

	@Value("${rabbitmq.queue}")
	private String queue;

	@Value("${rabbitmq.routing-key}")
	private String routingKey;

	// crea el exchange. El true es durable (sobrevive reinicios), el false es que no se auto-elimina.
	@Bean
	public DirectExchange exchange(){
		return new DirectExchange(exchange, true, false);
	}

	// crea la queue. El true es durable.
	@Bean
	public Queue queue(){
		return new Queue(queue, true);
	}

	// conecta la queue al exchange con la routing key product.created.
	@Bean
	public Binding binding(Queue queue, DirectExchange directExchange){
		return BindingBuilder.bind(queue).to(directExchange).with(routingKey);
	}

	// serializa los objetos Java a JSON automáticamente al publicar y deserializa al consumir.
	@Bean
	public Jackson2JsonMessageConverter messageConverter(){
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
		var template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(messageConverter());
		return template;
	}
}
