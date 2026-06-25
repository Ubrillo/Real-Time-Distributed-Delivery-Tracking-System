package com.ubrillo.ubrillodeliverysystem.Logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Getter;

/**
 * Embeddable value object representing a geographic location (latitude/longitude).
 */
@Getter
@Embeddable
public class Location {

    @JsonProperty("lat")
    private double longitude;

    @JsonProperty("lng")
    private double latitude;

    /**
     * Constructs a location using latitude and longitude values.
     */
    public Location(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Constructs a location from a Coordinate object.
     */
    public Location(Coordinate coordinate){
        setLocation(coordinate);
    }

    /**
     * Default constructor required for JPA.
     */
    public Location(){}

    /**
     * Sets longitude value.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Sets latitude value.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Updates location using latitude and longitude.
     */
    public void setLocation(double latitude, double longitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Updates location using a Coordinate object.
     */
    public void setLocation(Coordinate xy){
        this.longitude = xy.longitude();
        this.latitude = xy.latitude();
    }
}

