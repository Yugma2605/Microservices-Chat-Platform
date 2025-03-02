package com.example.shared;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
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
