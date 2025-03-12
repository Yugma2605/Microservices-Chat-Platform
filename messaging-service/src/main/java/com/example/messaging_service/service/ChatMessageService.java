package com.example.messaging_service.chat;

import com.example.shared.ChatNotification;
import com.example.messaging_service.chatroom.ChatRoomService;
import com.example.messaging_service.dto.FileRequest;
import com.example.messaging_service.dto.FileResponse;
import com.example.messaging_service.service.KafkaMessageProducer;
import com.example.messaging_service.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final WebClient webClient;
    private final KafkaMessageProducer kafkaMessageProducer;  // Injected KafkaMessageProducer bean
    private final UserService userService;

    public ChatMessage save(ChatMessage chatMessage) throws JsonProcessingException {
        var chatId = chatRoomService.getChatRoomId(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                true
        ).orElseThrow();

        log.info("ChatID assigned ::::::::::::::::: " + chatId);
        chatMessage.setChatId(chatId);
        chatMessageRepository.save(chatMessage);

        ChatNotification chatNotification = ChatNotification.builder()
                .id(chatMessage.getId())
                .senderId(chatMessage.getSenderId())
                .recipientId(chatMessage.getRecipientId())
                .senderEmail(getEmailFromUserId(chatMessage.getSenderId()))
                .recipientEmail(getEmailFromUserId(chatMessage.getRecipientId()))
                .senderUserName(getFullnameFromUserId(chatMessage.getSenderId()))
                .recipientUserName(getFullnameFromUserId(chatMessage.getRecipientId()))
                .filePath(chatMessage.getFilePath())
                .fileSize(chatMessage.getFileSize())
                .fileName(chatMessage.getFileName())
                .content(chatMessage.getContent())
                .build();

        // Use the KafkaMessageProducer bean to send a message
        kafkaMessageProducer.sendMessage("message-events", chatNotification);

        return chatMessage;
    }
    public String getEmailFromUserId(String userId){
        return userService.getEmailfromId(userId).get().getEmail();
    }


    public String getFullnameFromUserId(String userId){
        return userService.getFullnamefromId(userId).get().getFullName();
    }

    public FileResponse uploadFile(MultipartFile file, String userId, String userName) {
        FileRequest fileRequest = FileRequest.builder()
                .file(file)
                .userId(userId)
                .userName(userName)
                .build();

        FileResponse fileResponse = webClient.post()
                .uri("http://localhost:8084/api/upload")
                .bodyValue(fileRequest)
                .retrieve()
                .bodyToMono(FileResponse.class).block();

        return fileResponse;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(
                senderId,
                recipientId,
                false
        );

        return chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());
    }
}
