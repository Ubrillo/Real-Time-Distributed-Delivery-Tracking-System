package com.ubrillo.ubrillodeliverysystem.StateManagement;

import com.ubrillo.ubrillodeliverysystem.Logic.Location;
import com.ubrillo.ubrillodeliverysystem.Logic.RequestStatus;

import java.time.Instant;

public record OrderState(
        String requestId,
        RequestStatus status,
        Instant updatedAt,
        Location location,
        String history
) {}