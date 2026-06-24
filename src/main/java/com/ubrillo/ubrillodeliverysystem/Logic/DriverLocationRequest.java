package com.ubrillo.ubrillodeliverysystem.Logic;

import lombok.Getter;
import lombok.Setter;

public class DriverLocationRequest {
    @Getter
    @Setter
    private String driverId;
    @Getter
    @Setter
    private double latitude;
    @Getter
    @Setter
    private double longitude;

    // getters and setters
}