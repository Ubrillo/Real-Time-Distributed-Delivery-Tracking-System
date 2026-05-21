package com.ubrillo.ubrillodeliverysystem.Events;

public record Notification(
        String sender,
        String recipient,
        String description,
        String time,
        String properties,
        String message
) {}
