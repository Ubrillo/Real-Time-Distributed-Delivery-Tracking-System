package com.ubrillo.ubrillodeliverysystem.Logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Location {

    @JsonProperty("lat")
    private double longitude;

    @JsonProperty("lng")
    private double  latitude;

    public Location(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public Location(){}

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLocation(double latitude, double longitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setLocation(Coordinate xy){
        this.longitude = xy.longitude();
        this.latitude = xy.latitude();
    }
}
