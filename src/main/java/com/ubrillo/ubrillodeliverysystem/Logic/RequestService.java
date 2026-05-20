package com.ubrillo.ubrillodeliverysystem.Logic;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class RequestService {
    public RequestService(){}
    public Request createRequest(Request request){
        if (isValidRequest(request)){
            request.setRequestId(generateTrackingId());
            request.setDescription(request.getDescription());
            request.setCurrentLocation(new Location(0.1, 0.2));
            request.setDeliveryLocation(new Location(0.5, 0.7));
            request.setTime(getCurrentTime());

//             System.out.println("new request created...");
             return request;
        }
        System.out.println("unsucessful...");
        return null;
    }

    public boolean isValidRequest(Request request){
        return (request.getCustomerName() != null &&
                request.getDeliveryZone() != null);
//                (request.getPickupAdress() != null ) &&
//                (request.getDeliveryAdress() != null) &&
//                (request.getDeliveryPostcode() != null) &&
//                (request.getPickupPostcode() != null);
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
}
