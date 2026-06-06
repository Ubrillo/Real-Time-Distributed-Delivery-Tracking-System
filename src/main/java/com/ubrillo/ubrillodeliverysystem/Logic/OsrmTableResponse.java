package com.ubrillo.ubrillodeliverysystem.Logic;

import java.util.List;

public record OsrmTableResponse(
        String code,
        List<List<Double>> durations
) {}
