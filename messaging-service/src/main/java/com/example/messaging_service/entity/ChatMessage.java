package com.example.messaging_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "chat_messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatId;
    private String senderId;
    private String recipientId;
    private String content;
    private String senderUserName;
    private String recipientUserName;

    private String filePath;
    private String fileName;
    private String fileSize;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
}
