package com.ubrillo.ubrillodeliverysystem.Logic;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Response model used for GPS tracking updates, extending Request data structure.
 */
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

    /**
     * Default constructor.
     */
    public GpsTrackingResponse(){}

    /**
     * Constructs a GPS tracking response with current and destination locations.
     */
    public GpsTrackingResponse(Location currentLocation, Location destination) {
        this.destination = destination;
        this.currentLocation = currentLocation;
    }

    /**
     * Constructs a GPS tracking response with location and status.
     */
    public GpsTrackingResponse(
            Location currentLocation,
            Location destination,
            RequestStatus status
    ) {
        this.currentLocation = currentLocation;
        this.destination = destination;
        this.status = status;
    }

    /**
     * Returns string representation of GPS tracking response.
     */
    @Override
    public String toString() {
        return "GpsTrackingResponse{" +
                "currentLocation=" + currentLocation +
                ", destination=" + destination +
                '}';
    }
}