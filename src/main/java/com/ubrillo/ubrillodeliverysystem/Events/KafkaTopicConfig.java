package com.ubrillo.ubrillodeliverysystem.Events;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka topic configuration class responsible for defining application topics.
 */
@Configuration
public class KafkaTopicConfig {

    /**
     * Defines the Kafka topic used for publishing order-related events.
     */
    @Bean
    public NewTopic orderEventsTopic(){
        return TopicBuilder.name("order-events")
                //.partitions(2)
                //.replicas(1)
                .build();
    }

    /**
     * Defines the Kafka topic used for tracking order state changes.
     */
    @Bean
    public NewTopic orderStateManagement(){
        return TopicBuilder.name("tracking-events")
                //.partitions(2)
                //.replicas(1)
                .build();
    }
}