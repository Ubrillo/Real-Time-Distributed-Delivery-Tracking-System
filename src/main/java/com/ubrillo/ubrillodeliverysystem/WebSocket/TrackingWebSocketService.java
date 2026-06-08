package com.ubrillo.ubrillodeliverysystem.WebSocket;

import com.ubrillo.ubrillodeliverysystem.Logic.Coordinate;
import com.ubrillo.ubrillodeliverysystem.Logic.signalGPS;
import com.ubrillo.ubrillodeliverysystem.StateManagement.OrderState;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TrackingWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendTrackingUpdate(OrderState update){
        messagingTemplate.convertAndSend(
               "/gps/topic" + update.requestId(),
                update
        );
    }
}
