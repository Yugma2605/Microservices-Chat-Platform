package com.example.messaging_service.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageProducer.class);
    private KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaMessageProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        LOGGER.info("KafkaTemplate injected successfully: {}", kafkaTemplate != null);
    }

    public void sendMessage(String topic, Object message) {
        LOGGER.info("Producing message to {}: {}", topic, message);
        kafkaTemplate.send(topic, message);
    }
}
