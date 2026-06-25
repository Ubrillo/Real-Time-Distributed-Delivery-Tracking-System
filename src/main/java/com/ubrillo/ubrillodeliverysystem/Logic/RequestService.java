package com.ubrillo.ubrillodeliverysystem.Logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Service responsible for creating and validating delivery requests,
 * including ID generation and initial data enrichment.
 */
@Service
public class RequestService {

    @Autowired
    GeocodingService geocodingService;

    /**
     * Default constructor.
     */
    public RequestService(){}

    /**
     * Creates and initializes a valid delivery request.
     *
     * @param request incoming request data
     * @return initialized Request or null if invalid
     */
    public Request createRequest(Request request){
        if (isValidRequest(request)){
            request.setRequestId(generateTrackingId());
            request.setDescription(request.getDescription());
            request.setCurrentLocation(new Location(-0.1281105, 51.507458));

            Coordinate coordinate = new Coordinate(0., 0.);//getDeliveryCoordinate(request);
            request.setDeliveryLocation(new Location(coordinate.longitude(), coordinate.latitude()));

            request.setUpdateAt(Instant.now());
            request.setStatus(RequestStatus.CREATED);

            return request;
        }

        System.out.println("unsucessful...");
        return null;
    }

    /**
     * Validates whether a request contains required fields.
     */
    public boolean isValidRequest(Request request){
        return (request.getCustomerName() != null &&
                request.getUserEmail() != null &&
                request.getPostCode() != null);
    }

    /**
     * Generates a unique tracking ID for a request.
     */
    public String generateTrackingId(){
        return "REQ-" +
                System.currentTimeMillis() + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Placeholder for request push logic.
     */
    public void pushRequest(){

    }

    /**
     * Returns current system time in formatted string.
     */
    public String getCurrentTime(){
        LocalDateTime time = LocalDateTime.now();
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Retrieves delivery coordinates using geocoding service.
     */
    public Coordinate getDeliveryCoordinate(Request request){
        return geocodingService.getCoordinates(request.getPostCode());
    }
}