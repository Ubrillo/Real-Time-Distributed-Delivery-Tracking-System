package com.ubrillo.ubrillodeliverysystem.WebSocket;

import com.ubrillo.ubrillodeliverysystem.Events.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//@RequiredArgsConstructor
//@Slf4j
@Service
public class WebSocketEventListener {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate){
       this.messagingTemplate = messagingTemplate;
    }

    public void sendOrderUpdate(Notification update){
        messagingTemplate.convertAndSend(
                "/topic/orders/"+update.orderId(),
                update
        );
    }
}


