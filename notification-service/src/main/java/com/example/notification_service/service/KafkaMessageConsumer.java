package com.example.notification_service.service;

import com.example.messaging_service.chat.ChatMessage;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageConsumer {

    private final GmailService emailService;

    @KafkaListener(topics = "message-events", groupId = "message-notification-group")
    public void consumeMessage(ChatMessage chatMessage) throws MessagingException, IOException {
        log.info("Consumed event: {}", chatMessage);

        // Extract the recipient email and send a notification
        String recipientEmail = getEmailForUser(chatMessage.getRecipientId());
        String subject = "New Message Received!";
        String body = String.format("You have a new message from %s: %s",
                chatMessage.getSenderId(), chatMessage.getContent());

        emailService.sendEmail(recipientEmail, subject, body);
    }

    private String getEmailForUser(String recipientId) {
        // Implement logic to retrieve email based on recipient ID
        return "anshray11@gmail.com";
    }
}
