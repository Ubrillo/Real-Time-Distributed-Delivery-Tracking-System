package com.ubrillo.ubrillodeliverysystem.Logic;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Builder
public class GpsTrackingResponse extends Request {
    @Getter
    @Setter
    private Location currentLocation;

    @Getter
    @Setter
    private Location destination;

    @Getter
    @Setter
    private RequestStatus status;

    public GpsTrackingResponse(){}

    public GpsTrackingResponse(Location currentLocation, Location destination) {
        this.destination = destination;
        this.currentLocation = currentLocation;
    }
    public GpsTrackingResponse(
            Location currentLocation,
            Location destination,
            RequestStatus status
    ) {
        this.currentLocation = currentLocation;
        this.destination = destination;
        this.status = status;
    }

    @Override
    public String toString() {
        return "GpsTrackingResponse{" +
                "currentLocation=" + currentLocation +
                ", destination=" + destination +
                '}';
    }
}

