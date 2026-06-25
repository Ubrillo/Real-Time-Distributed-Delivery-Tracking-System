package com.ubrillo.ubrillodeliverysystem.Events;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service responsible for sending email notifications related to order updates.
 */
@Service
public class EmailNotificationService {

    /**
     * Spring Mail sender used to dispatch email messages.
     */
    private JavaMailSender mailSender;

    /**
     * Default constructor.
     */
    public EmailNotificationService() {}

    /**
     * Sends an email notification to the user based on order status updates.
     *
     * @param event Notification event containing user email and order status
     */
    public void sendOrdUpdateEmail(Notification event){
        SimpleMailMessage message = new SimpleMailMessage();

        String messageContent = "";

        // Determine email message content based on order status
        switch (event.orderStatus()){
            case CREATED -> messageContent = "📦Your order has been received";
            case DISPATCHED -> messageContent = "📦Your order has been dispatched";
            case CANCELLED -> messageContent = "📦Your order has been cancelled";
            case DELIVERED -> messageContent = "📦Your order has been delivered";
            case OUTFORDELIVERY -> messageContent = "📦Your order is out for delivery!!!";
        }

        // Configure email details
        message.setTo(event.userEmail());
        message.setSubject("your order status changed");
        message.setText(messageContent);

        // Send email
        mailSender.send(message);
    }
}