package com.kafkatech.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KafkaTopicConfig {

    @Value("${kafka-topic.name}")
    private String topicName;
    @Value("${kafka-topic.partitions}")
    private int topicPartitions;
    @Value("${kafka-topic.replication-factor}")
    private int replicationFactor;

    @Bean
    public NewTopic createTopic(){
        System.out.println("TOPIC " + topicName + " - " + topicPartitions + " - " + replicationFactor);
        return new NewTopic(topicName, topicPartitions, (short) replicationFactor);
    }
}
