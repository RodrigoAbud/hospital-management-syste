package com.fiap.atividade3.service;

import com.fiap.atividade3.config.AsyncConfig;
import com.fiap.atividade3.model.entity.Consulta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for handling asynchronous messaging between services
 * TEMPORARILY DISABLED - RabbitMQ not available in development
 */
//@Service
public class AsyncMessagingService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncMessagingService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Publish consultation created event
     */
    public void publishConsultaCreated(Consulta consulta) {
        try {
            logger.info("Publishing consultation created event for consultation ID: {}", consulta.getId());
            rabbitTemplate.convertAndSend(
                AsyncConfig.CONSULTA_EXCHANGE,
                AsyncConfig.CONSULTA_CREATED_ROUTING_KEY,
                consulta
            );
        } catch (Exception e) {
            logger.error("Error publishing consultation created event", e);
        }
    }

    /**
     * Publish consultation updated event
     */
    public void publishConsultaUpdated(Consulta consulta) {
        try {
            logger.info("Publishing consultation updated event for consultation ID: {}", consulta.getId());
            rabbitTemplate.convertAndSend(
                AsyncConfig.CONSULTA_EXCHANGE,
                AsyncConfig.CONSULTA_UPDATED_ROUTING_KEY,
                consulta
            );
        } catch (Exception e) {
            logger.error("Error publishing consultation updated event", e);
        }
    }

    /**
     * Handle consultation created events
     */
    @RabbitListener(queues = AsyncConfig.CONSULTA_CREATED_QUEUE)
    public void handleConsultaCreated(Consulta consulta) {
        logger.info("Received consultation created event for consultation ID: {}", consulta.getId());
        
        // Here you can implement additional business logic such as:
        // - Sending notifications to relevant users
        // - Updating statistics
        // - Triggering other workflows
        // - Logging for audit purposes
        
        // Example: Log the event for audit purposes
        logger.info("Consultation created - Patient: {}, Doctor: {}, Date: {}", 
                   consulta.getPaciente().getNome(),
                   consulta.getMedico().getNome(),
                   consulta.getDataConsulta());
    }

    /**
     * Handle consultation updated events
     */
    @RabbitListener(queues = AsyncConfig.CONSULTA_UPDATED_QUEUE)
    public void handleConsultaUpdated(Consulta consulta) {
        logger.info("Received consultation updated event for consultation ID: {}", consulta.getId());
        
        // Here you can implement additional business logic such as:
        // - Sending notifications about updates
        // - Updating related records
        // - Triggering follow-up actions
        
        // Example: Log the event for audit purposes
        logger.info("Consultation updated - ID: {}, Updated by: {}", 
                   consulta.getId(),
                   consulta.getMedico().getNome());
    }
}
