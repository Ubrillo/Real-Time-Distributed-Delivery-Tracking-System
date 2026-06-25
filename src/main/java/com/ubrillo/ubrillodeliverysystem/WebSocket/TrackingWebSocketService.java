package com.ubrillo.ubrillodeliverysystem.WebSocket;

import com.ubrillo.ubrillodeliverysystem.Cache.OrderState;
import com.ubrillo.ubrillodeliverysystem.Logic.GpsTrackingResponse;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Service responsible for sending real-time tracking updates
 * to connected WebSocket clients.
 */
@AllArgsConstructor
@Service
public class TrackingWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Sends a GPS tracking update to a specific user/topic channel.
     *
     * @param update tracking response containing location and order details
     */
    public void sendTrackingUpdate(GpsTrackingResponse update){
        messagingTemplate.convertAndSend(
                "/gps/topic/user/" + update.getRequestId(),
                update
        );
    }
}
