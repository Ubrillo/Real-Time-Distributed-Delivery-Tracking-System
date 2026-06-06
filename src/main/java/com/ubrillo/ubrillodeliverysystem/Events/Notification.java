package com.ubrillo.ubrillodeliverysystem.Events;

import com.ubrillo.ubrillodeliverysystem.Logic.Request;
import com.ubrillo.ubrillodeliverysystem.Logic.RequestStatus;

public record Notification(
        String sender,
        String recipient,
        String description,
        String time,
        String orderId,
        String message,
        String userEmail,
        RequestStatus orderStatus
)   {}