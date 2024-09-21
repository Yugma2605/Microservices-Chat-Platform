package com.example.messaging_service.chat;

import com.example.messaging_service.chatroom.ChatRoomService;
import com.example.messaging_service.dto.FileRequest;
import com.example.messaging_service.dto.FileResponse;
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

    public ChatMessage save(ChatMessage chatMessage){
        var chatId = chatRoomService.getChatRoomId(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                true
        ).orElseThrow();
        log.info("ChatID assigned ::::::::::::::::: "+chatId);
        chatMessage.setChatId(chatId);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }


    public FileResponse uploadFile(MultipartFile file, String userId, String userName){
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
    public List<ChatMessage> findChatMessages(String senderId, String recipientId){
        var chatId = chatRoomService.getChatRoomId(
                senderId,
                recipientId,
                false
        );

        return chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());
    }
}
