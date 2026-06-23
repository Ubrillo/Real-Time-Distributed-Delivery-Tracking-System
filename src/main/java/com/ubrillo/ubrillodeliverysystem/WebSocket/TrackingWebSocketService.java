package com.ubrillo.ubrillodeliverysystem.WebSocket;

import com.ubrillo.ubrillodeliverysystem.Cache.OrderState;
import com.ubrillo.ubrillodeliverysystem.Logic.GpsTrackingResponse;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TrackingWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendTrackingUpdate(GpsTrackingResponse update){
        messagingTemplate.convertAndSend(
               "/gps/topic/user/" + update.getRequestId(),
                update
        );
    }
}
