package com.example.messaging_service.chat;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    @Id
    private Long id;
    private String senderId;
    private String recipientId;
    private String content;
//    private FileInfo fileInfo;
}
