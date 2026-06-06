package com.ubrillo.ubrillodeliverysystem.Logic;

import java.util.List;

public record RouteResult(
        List<DeliveryStop> stops,
        long totalDriveSeconds
) {}