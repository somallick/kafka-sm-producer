package com.kafkatech.service;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessagePublisher.class);
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private NewTopic kafkaTopic;

    public void sendMessageToTopic(String message) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(kafkaTopic.name(), message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        message + "] due to : " + ex.getMessage());
            }
        });
    }
}
