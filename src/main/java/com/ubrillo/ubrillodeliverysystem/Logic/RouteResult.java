package com.ubrillo.ubrillodeliverysystem.Logic;

import java.util.List;

/**
 * Immutable result representing an optimized delivery route
 * including ordered stops and total travel time.
 */
public record RouteResult(
        List<DeliveryStop> stops,
        long totalDriveSeconds
) {}
