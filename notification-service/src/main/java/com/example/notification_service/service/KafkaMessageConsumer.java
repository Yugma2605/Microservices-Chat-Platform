package com.example.notification_service.service;

import com.example.shared.ChatNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.example.notification_service.dto.EmailResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageConsumer {

    private final EmailService emailService; // Autowiring EmailService to send emails
    @KafkaListener(topics = "message-events", groupId = "message-notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeMessage(ChatNotification chatNotification) throws MessagingException, IOException {
        log.info("Consumed chatNotification message: {}", chatNotification);


        String recipientEmail = chatNotification.getRecipientEmail();
        String subject = "New Message Received!";
        String body = String.format("You have a new message from %s: %s",
                chatNotification.getSenderId(), chatNotification.getContent());


        // Call the EmailService to send the email
        String emailResponse = emailService.sendSimpleMail(chatNotification);

        // Log the result of the email sending operation
        log.info("Email sent: {}", emailResponse);
    }
}
