package com.ubrillo.ubrillodeliverysystem.Events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {
    @KafkaListener(
            topics = "order-events",
            groupId = "tracking-service-group"
    )
    public void consumeOrderCreated(OrderEvent event){
           System.out.println("📦received order event "+event.getRequestId());
    }
}
