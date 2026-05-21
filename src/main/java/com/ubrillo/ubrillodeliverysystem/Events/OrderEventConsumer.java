package com.ubrillo.ubrillodeliverysystem.Events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {
    @KafkaListener(
            topics = "order-events",
            groupId = "tracking-service-group"
    )
    public void consumeOrderCreated(Notification event){
        String message =
                "\n=================[Notification]================"+
                "\nsender: "+event.sender()+
                "\nrecipient: "+event.recipient()+
                "\ndescription: "+event.description()+
                "\ntime: "+event.time()+
                "\ndetails: "+event.properties()+
                "\nmessage: "+event.message()+
                "\n"+"=====================[END]====================";
        System.out.println(message);
    }
}
