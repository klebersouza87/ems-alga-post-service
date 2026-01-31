package com.ems.alga.posts.post.service.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_PROCESSED_MESSAGE = "post-service.post-processing-result.v1.q";
    public static final String DEAD_LETTER_QUEUE_PROCESSED_MESSAGE = "post-service.post-processing-result.v1.dlq";

    public static final String QUEUE_TEXT_PROCESSOR_SERVICE = "text-processor-service.post-processing.v1.q";
    public static final String DEAD_LETTER_QUEUE_TEXT_PROCESSOR_SERVICE = "text-processor-service.post-processing.v1.dlq";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue deadLetterQueueTextProcessorService() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_TEXT_PROCESSOR_SERVICE).build();
    }

    @Bean
    public Queue queuePostReceived() {
        Map<String, Object> args = Map.of(
                "x-dead-letter-exchange", "",
                "x-dead-letter-routing-key", DEAD_LETTER_QUEUE_TEXT_PROCESSOR_SERVICE
        );
        return QueueBuilder.durable(QUEUE_TEXT_PROCESSOR_SERVICE).withArguments(args).build();
    }

    @Bean
    public Queue deadLetterQueueProcessedMessage() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_PROCESSED_MESSAGE).build();
    }

    @Bean
    public Queue queueProcessedMessage() {
        Map<String, Object> args = Map.of(
                "x-dead-letter-exchange", "",
                "x-dead-letter-routing-key", DEAD_LETTER_QUEUE_PROCESSED_MESSAGE
        );
        return QueueBuilder.durable(QUEUE_PROCESSED_MESSAGE).withArguments(args).build();
    }

}
