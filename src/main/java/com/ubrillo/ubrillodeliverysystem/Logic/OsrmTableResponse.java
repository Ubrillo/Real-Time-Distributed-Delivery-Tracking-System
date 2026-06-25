package com.ubrillo.ubrillodeliverysystem.Logic;

import java.util.List;

/**
 * DTO representing OSRM Table API response containing travel durations matrix.
 */
public record OsrmTableResponse(
        String code,
        List<List<Double>> durations
) {}