package com.ubrillo.ubrillodeliverysystem.Logic;

public enum Zone {
    NORTHWEST("northWest"),
    SOUTHWEST("southWest"),
    NORTHEAST("northEast"),
    SOUTHEAST("southEast");

    private String zone;

    private  Zone(String st){zone = st;}
    public String toString(){return zone;}


}