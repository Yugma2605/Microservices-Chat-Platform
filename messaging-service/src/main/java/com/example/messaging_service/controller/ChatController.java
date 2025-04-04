package com.example.messaging_service.controller;


import com.example.messaging_service.entity.ChatMessage;
import com.example.messaging_service.service.ChatMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/messaging")
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
//    @CrossOrigin("http://localhost:63342/")
    public void processMessage(@Payload ChatMessage chatMessage) throws JsonProcessingException {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        log.info(savedMsg.getChatId()+" "+savedMsg.getRecipientId());
        // sending msg to queue {user}/queue/messages

        ChatMessage chatNotification = ChatMessage.builder()
                .id(savedMsg.getId())
                .senderId(savedMsg.getSenderId())
                .recipientId(savedMsg.getRecipientId())
                .senderUserName(savedMsg.getSenderUserName())
                .recipientUserName(savedMsg.getRecipientUserName())
                .filePath(savedMsg.getFilePath())
                .fileSize(savedMsg.getFileSize())
                .fileName(savedMsg.getFileName())
                .content(savedMsg.getContent())
                .build();


        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                chatNotification
        );

    }


    @GetMapping("/messages/{senderId}/{recipientId}")
//    @CrossOrigin("http://localhost:63342/")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable("senderId") String senderId,
            @PathVariable("recipientId") String recipientId
    ){
        log.info("senderId:"+senderId);
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
