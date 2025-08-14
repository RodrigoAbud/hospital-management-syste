package com.fiap.atividade3.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuration for asynchronous messaging using RabbitMQ
 * TEMPORARILY DISABLED - RabbitMQ not available in development
 */
//@Configuration
//@EnableAsync
// @ConditionalOnProperty(name = "spring.rabbitmq.host")
public class AsyncConfig {

    public static final String CONSULTA_EXCHANGE = "consulta.exchange";
    public static final String CONSULTA_CREATED_QUEUE = "consulta.created.queue";
    public static final String CONSULTA_UPDATED_QUEUE = "consulta.updated.queue";
    public static final String CONSULTA_CREATED_ROUTING_KEY = "consulta.created";
    public static final String CONSULTA_UPDATED_ROUTING_KEY = "consulta.updated";

    /**
     * Exchange for consultation events
     */
    @Bean
    public TopicExchange consultaExchange() {
        return new TopicExchange(CONSULTA_EXCHANGE);
    }

    /**
     * Queue for consultation created events
     */
    @Bean
    public Queue consultaCreatedQueue() {
        return QueueBuilder.durable(CONSULTA_CREATED_QUEUE).build();
    }

    /**
     * Queue for consultation updated events
     */
    @Bean
    public Queue consultaUpdatedQueue() {
        return QueueBuilder.durable(CONSULTA_UPDATED_QUEUE).build();
    }

    /**
     * Binding for consultation created events
     */
    @Bean
    public Binding consultaCreatedBinding() {
        return BindingBuilder
                .bind(consultaCreatedQueue())
                .to(consultaExchange())
                .with(CONSULTA_CREATED_ROUTING_KEY);
    }

    /**
     * Binding for consultation updated events
     */
    @Bean
    public Binding consultaUpdatedBinding() {
        return BindingBuilder
                .bind(consultaUpdatedQueue())
                .to(consultaExchange())
                .with(CONSULTA_UPDATED_ROUTING_KEY);
    }

    /**
     * JSON message converter for RabbitMQ
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitTemplate with JSON converter
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
