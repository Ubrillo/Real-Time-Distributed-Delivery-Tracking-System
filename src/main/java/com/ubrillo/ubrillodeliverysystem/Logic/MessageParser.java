package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.Events.Notification;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Utility component responsible for converting Request objects
 * into Notification events used for messaging and Kafka publishing.
 */
@Component
public class MessageParser {

    /**
     * Default constructor.
     */
    public MessageParser(){}

    /**
     * Parses a Request into a Notification event based on its status.
     *
     * @param order the order request to convert
     * @return Notification event representing the order state
     */
    public Notification parser(Request order){

        RequestStatus eventType = order.getStatus();

        String sender = "ubrillo-delivery@org.uk";
        String recipient = order.getCustomerName();
        String description = order.getDescription();

        String time = Instant.now().toString();

        String orderId = order.getRequestId();
        RequestStatus orderStatus = order.getStatus();

        String message = "";

        switch (eventType){
            case CREATED -> message = "📦Your order has been received";
            case DISPATCHED -> message = "📦Your order has been dispatched";
            case CANCELLED -> message = "📦Your order has been cancelled";
            case DELIVERED -> message = "📦Your order has been delivered";
            case OUTFORDELIVERY -> message = "📦Your order is out for delivery!!!";
        }

        return new Notification(
                sender,
                recipient,
                description,
                time,
                orderId,
                message,
                order.getUserEmail(),
                orderStatus
        );
    }
}

