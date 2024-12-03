package com.kafkatech.controller;

import com.kafkatech.service.KafkaMessagePublisher;
import com.kafkatech.service.KafkaMessagePublisher2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producer-app")
public class KafkaEventController {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventController.class);
    @Autowired
    private KafkaMessagePublisher kafkaMessagePublisher;
    @Autowired
    private KafkaMessagePublisher2 kafkaMessagePublisher2;

    @PostMapping("/publish/{id}")
    public ResponseEntity<?> publishMessage(@RequestBody String message, @PathVariable String id) {
        try {
            if(Integer.parseInt(id.trim())==1)
                kafkaMessagePublisher.sendMessageToTopic(message);
            else
                kafkaMessagePublisher2.sendMessageProcessWithCallbacks(message);
            return ResponseEntity.ok("message published successfully ..");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
