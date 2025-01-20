package com.example.messaging_service.service;

import com.example.shared.ChatNotification;
//import com.example.messaging_service.chat.ChatNotification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageProducer.class);
    private KafkaTemplate<String, ChatNotification> kafkaTemplate;

    public KafkaMessageProducer(KafkaTemplate<String, ChatNotification> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        LOGGER.info("KafkaTemplate injected successfully: {}", kafkaTemplate != null);
    }

    public void sendMessage(String topic, ChatNotification message) {
        LOGGER.info("Producing message to {}: {}", topic, message);
        kafkaTemplate.send(topic, message);
    }
}
