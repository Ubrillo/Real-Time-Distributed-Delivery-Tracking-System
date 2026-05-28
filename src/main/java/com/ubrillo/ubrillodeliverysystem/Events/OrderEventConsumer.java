package com.ubrillo.ubrillodeliverysystem.Events;

import com.ubrillo.ubrillodeliverysystem.StateManagement.OrderState;
import com.ubrillo.ubrillodeliverysystem.StateManagement.OrderStateStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderEventConsumer {
    @Autowired
    private  OrderStateStore stateStore;

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


    @KafkaListener(topics ="tracking-events")
    public void consumeOrderState(OrderEvent event){
        String history = addHistory(event);

        OrderState state = new OrderState(
                event.getRequestId(),
                event.getStatus(),
                event.getTime(),
                event.getLocation(),
                history
        );

        stateStore.updateState(state);
    }

    private String  addHistory(OrderEvent event){
        return "\n--------------[Latest Update]-------"+
                "\nstatus: "+event.getStatus()+
                "\ncurrent location: "+event.getLocation()+
                "\ntime: "+event.getUpdatedAt()+
                "\nID: "+event.getRequestId()+
                "\nInfo: "+event.getInfo()+
                "\n"+"------------[END]---------------";
    }
}
