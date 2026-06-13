package com.ubrillo.ubrillodeliverysystem.Events;

import com.ubrillo.ubrillodeliverysystem.Logic.*;
import com.ubrillo.ubrillodeliverysystem.Cache.OrderState;
import com.ubrillo.ubrillodeliverysystem.Cache.OrderStateStore;
import com.ubrillo.ubrillodeliverysystem.WebSocket.TrackingWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {
    @Autowired
    private GpsService gpsService;

    @Autowired
    private  OrderStateStore stateStore;

    @Autowired
    private  EmailNotificationService emailService;

    @Autowired
    TrackingWebSocketService trackingWebSocketService;

    @KafkaListener(
            topics = "order-events",
            groupId = "tracking-service-group"
    )
    private void consumeOrderCreated(Notification event){
        String message =
                "\n=================[Notification]================"+
                "\nsender: "+event.sender()+
                "\nrecipient: "+event.recipient()+
                "\ndescription: "+event.description()+
                "\ntime: "+event.time()+
                "\norder No: "+event.orderId()+
                "\nmessage: "+event.message()+
                "\n"+"=====================[END]====================";
        System.out.println(message);
        //emailService.sendOrdUpdateEmail(event);
    }

    @KafkaListener(topics ="tracking-events")
    private void consumeOrderState(OrderEvent event){
        String history = addHistory(event);

        OrderState state = new OrderState(
                event.getRequestId(),
                event.getStatus(),
                event.getTime(),
                event.getLocation(),
                event.getDestination(),
                history
        );
        stateStore.updateState(state);
    }

    @KafkaListener(topics="gps-tracker")
    private void consumeGpsSignal(signalGPS signal){
        gpsService.updateLocation(signal);
    }

    @KafkaListener(topics="order-tracking-udate", groupId="websocket-tracking-service")
    public void consumeTrackingUpdate(OrderState update){
        trackingWebSocketService.sendTrackingUpdate(
                new OrderState(
                        update.requestId(),
                        update.status(),
                        update.updatedAt(),
                        update.location(),
                        update.destination(),
                        update.history()
                )
        );
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