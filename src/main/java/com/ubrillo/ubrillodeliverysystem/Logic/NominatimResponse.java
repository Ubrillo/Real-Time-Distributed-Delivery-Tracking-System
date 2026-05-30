package com.ubrillo.ubrillodeliverysystem.Logic;

public class NominatimResponse {

    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String lat) {
        this.latitude = lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String lon) {
        this.longitude = lon;
    }
}