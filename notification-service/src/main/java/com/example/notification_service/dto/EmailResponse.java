// Java Program to Illustrate EmailDetails Class

package com.example.notification_service.dto;

// Importing required classes
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor

// Class
public class EmailResponse {

    // Class data members
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    private String senderUserName;
    private String recipientUserName;

    private String senderEmail;
    private String recipientEmail;

    private String filePath;
    private String fileName;
    private String fileSize;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
}