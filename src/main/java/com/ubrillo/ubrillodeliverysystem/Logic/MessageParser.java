package com.ubrillo.ubrillodeliverysystem.Logic;

import com.ubrillo.ubrillodeliverysystem.Events.Notification;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MessageParser {

    public MessageParser(){}

    public Notification parser(Request order){
        RequestStatus eventType = order.getStatus();
        String sender = "ubrillo-delivery@org.uk";
        String recipient = order.getCustomerName();
        String description = order.getDescription();
        String time = time = Instant.now().toString();
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
