package com.ubrillo.ubrillodeliverysystem.Events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, Notification> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, Notification> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(Notification event){
        kafkaTemplate.send("order-events", event.sender(), event);
    }
}
