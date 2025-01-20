package com.example.notification_service.service;

// Importing required classes
import java.io.File;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

//import com.example.notification_service.dto.ChatNotification;
import com.example.shared.ChatNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.example.notification_service.dto.EmailResponse;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    // Method 1
    // To send a simple email
    public String sendSimpleMail(ChatNotification details) {

        // Try block to check for exceptions
        try {

            // Check if the file exists
            boolean fileExists = false;
            String fileName = details.getFileName();
            log.info("You can see the file Name:"+fileName);
            if (fileName != null && !fileName.isEmpty()) {
                fileExists = true;
            }

            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipientEmail());

            // Prepare the email body
            String fileMessage = fileExists
                    ? String.format("A file %s is attached with this message",fileName)
                    : "No file is attached with this message.";

            String body = String.format(
                    "You have a new message from %s: %s\n\n%s",
                    details.getSenderId(),
                    details.getContent(),
                    fileMessage
            );

            mailMessage.setText(body);
            mailMessage.setSubject("You have a new notification");

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        }
    }
}
