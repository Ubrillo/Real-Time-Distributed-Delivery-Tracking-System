package com.ubrillo.ubrillodeliverysystem.Logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class RequestService {
    @Autowired
    GeocodingService geocodingService;

    public RequestService(){}

    public Request createRequest(Request request){
        if (isValidRequest(request)){
            request.setRequestId(generateTrackingId());
            request.setDescription(request.getDescription());
            request.setCurrentLocation(new Location( -0.1281105, 51.507458));

            Coordinate coordinate = getDeliveryCoordinate(request);

            request.setDeliveryLocation(new Location(coordinate.longitude(), coordinate.latitude()));
            request.setTime(getCurrentTime());

            //request.setTime(Instant.now().toString());
//          //System.out.println("new request created...")

             return request;
        }
        System.out.println("unsucessful...");
        return null;
    }

    public boolean isValidRequest(Request request){
        return (request.getCustomerName() != null &&
                request.getDeliveryZone() != null &&
                request.getEmailAddress() != null &&
                request.getPostCode() != null);

    }

    public String generateTrackingId(){
        return "REQ-"+
                System.currentTimeMillis()+"-"+
                UUID.randomUUID().toString().substring(0,8).toUpperCase();
    }

    public void pushRequest(){

    }
    public String  getCurrentTime(){
        LocalDateTime time = LocalDateTime.now();
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Coordinate getDeliveryCoordinate(Request request){
        return geocodingService.getCoordinates(request.getPostCode());
    }
}
