package com.ubrillo.ubrillodeliverysystem.Cache;

import com.ubrillo.ubrillodeliverysystem.Logic.Location;
import com.ubrillo.ubrillodeliverysystem.Logic.RequestStatus;

import java.io.Serializable;

public record OrderState(
        String requestId,
        RequestStatus status,
        String updatedAt,
        Location location,
        Location destination,
        String history

) implements Serializable {}