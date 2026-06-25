package com.ubrillo.ubrillodeliverysystem.WebSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration for enabling STOMP-based messaging
 * and configuring message broker destinations.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registers STOMP endpoints used by clients to connect to WebSocket server.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws/user")
                //.setAllowedOrigins("*")
                .setAllowedOrigins("http://localhost:63342")
                .withSockJS();
    }

    /**
     * Configures message broker prefixes for routing messages
     * between server and subscribed clients.
     */
    @Override
    public void configureMessageBroker (MessageBrokerRegistry config){
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker(
                "/gps/topic/user",
                "/gps/topic/driver"
        );
    }
}
