package com.ubrillo.ubrillodeliverysystem.Logic;

import lombok.Getter;

/**
 * Data model representing a GPS signal update sent by a driver/device.
 */
public class signalGPS {

    @Getter
    private Coordinate coordinate;

    @Getter
    private String sender;

    /**
     * Default constructor.
     */
    public signalGPS(){}
}