package com.ubrillo.ubrillodeliverysystem.Events;

import com.ubrillo.ubrillodeliverysystem.Logic.signalGPS;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka producer responsible for publishing order, tracking, and GPS events.
 */
@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, Notification> publisher1;
    private final KafkaTemplate<String, OrderEvent> publisher2;
    private final KafkaTemplate<String, signalGPS> gpsPublisher;

    /**
     * Constructor injection of Kafka templates for different event types.
     */
    public OrderEventProducer(KafkaTemplate<String, Notification> kafkaTemplate,
                              KafkaTemplate<String, OrderEvent> kafkaTemplate2,
                              KafkaTemplate<String, signalGPS> gpsPublisher
    )
    {
        this.publisher1 = kafkaTemplate;
        this.publisher2 = kafkaTemplate2;
        this.gpsPublisher = gpsPublisher;
    }

    /**
     * Publishes an order creation/notification event to Kafka.
     */
    public void publishOrderCreated(Notification event){
        publisher1.send("order-events", event.sender(), event);
    }

    /**
     * Publishes order state tracking updates to Kafka.
     */
    public void publishOrderStateTracker(OrderEvent event){
        publisher2.send("tracking-events", event);
    }

    /**
     * Publishes GPS location updates to Kafka.
     */
    public void publishGpsUpdate(signalGPS event){
        gpsPublisher.send("gps-tracker", event);
    }
}