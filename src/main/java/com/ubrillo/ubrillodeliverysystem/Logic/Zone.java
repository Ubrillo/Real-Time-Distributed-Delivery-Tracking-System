package com.ubrillo.ubrillodeliverysystem.Logic;

/**
 * Enum representing geographical delivery zones used for routing and dispatching orders.
 */
public enum Zone {

    NORTHWEST("northWest"),
    SOUTHWEST("southWest"),
    NORTHEAST("northEast"),
    SOUTHEAST("southEast");

    private String zone;

    /**
     * Constructs a zone with a human-readable name.
     */
    private Zone(String st){
        zone = st;
    }

    /**
     * Returns human-readable zone name.
     */
    @Override
    public String toString(){
        return zone;
    }
}