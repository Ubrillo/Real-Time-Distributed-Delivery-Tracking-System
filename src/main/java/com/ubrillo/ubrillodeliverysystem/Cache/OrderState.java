package com.ubrillo.ubrillodeliverysystem.Cache;

import com.ubrillo.ubrillodeliverysystem.Logic.Location;
import com.ubrillo.ubrillodeliverysystem.Logic.RequestStatus;

import java.io.Serializable;
import java.time.Instant;

public record OrderState(
        String requestId,
        RequestStatus status,
        Instant updatedAt,
        Location location,
        Location destination,
        String customerName,
        String description,
        String userEmail,
        String deliveryAddress,
        String postCode,
        String history

) implements Serializable {}