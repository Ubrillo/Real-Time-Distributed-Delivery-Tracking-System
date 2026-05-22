package com.ubrillo.ubrillodeliverysystem.Events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, Notification> publisher1;
    private final KafkaTemplate<String, OrderEvent> publisher2;

    public OrderEventProducer(KafkaTemplate<String, Notification> kafkaTemplate,
                              KafkaTemplate<String, OrderEvent> kafkaTemplate2){
        this.publisher1 = kafkaTemplate;
        this.publisher2 = kafkaTemplate2;
    }

    public void publishOrderCreated(Notification event){
        publisher1.send("order-events", event.sender(), event);
    }

    public void orderStateTracker(OrderEvent event){
        publisher2.send("tracking-events", event);
    }
}
