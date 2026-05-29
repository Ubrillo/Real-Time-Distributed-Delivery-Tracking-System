package com.ubrillo.ubrillodeliverysystem.Events;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.ubrillo.ubrillodeliverysystem.Logic.RequestStatus.*;


@Service
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    public EmailNotificationService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendOrdUpdateEmail(Notification event){
        SimpleMailMessage message = new SimpleMailMessage();

        String messageContent = "";

        switch (event.orderStatus()){
            case CREATED -> messageContent = "📦Your order has been received";
            case DISPATCHED -> messageContent = "📦Your order has been dispatched";
            case CANCELLED -> messageContent = "📦Your order has been cancelled";
            case DELIVERED -> messageContent = "📦Your order has been delivered";
            case OUTFORDELIVERY -> messageContent = "📦Your order is out for delivery!!!";
        }

        message.setTo(event.userEmail());
        message.setSubject("your order status changed");
        message.setText(messageContent);
        mailSender.send(message);
    }
}
