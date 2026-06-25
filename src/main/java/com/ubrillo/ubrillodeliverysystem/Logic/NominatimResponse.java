 package com.ubrillo.ubrillodeliverysystem.Logic;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO representing a response from the Nominatim geocoding API.
 */
public class NominatimResponse {

    @JsonProperty("lat")
    private String latitude;

    @JsonProperty("lon")
    private String longitude;

    /**
     * Returns latitude value as a string.
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude value from API response.
     */
    public void setLatitude(String lat) {
        this.latitude = lat;
    }

    /**
     * Returns longitude value as a string.
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude value from API response.
     */
    public void setLongitude(String lon) {
        this.longitude = lon;
    }
}

