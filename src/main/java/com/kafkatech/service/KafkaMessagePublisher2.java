package com.kafkatech.service;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class KafkaMessagePublisher2 {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessagePublisher2.class);
    @Autowired
    private NewTopic kafkaTopic;

    public void sendMessageProcess(String message) {

        log.info("Inside sendMessageProcess()");

        Properties properties = new Properties();

        //connect to localhost
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");

        //set producer serializer
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create producer
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(kafkaTopic.name(), message);

        // send data - asynchronous
        kafkaProducer.send(producerRecord);

        //tell the producer to send all data and block until done - synchronous
//        kafkaProducer.flush();

        // flush and close the producer
        kafkaProducer.close();
    }

    public void sendMessageProcessWithCallbacks(String message) {

        log.info("Inside sendMessageProcessWithCallbacks()");

        Properties properties = new Properties();
        //connect to localhost and set producer serializer
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create producer
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        //Record to be sent to kafka topic
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(kafkaTopic.name(), message);

        //Record to be sent to kafka topic with key - Please check ProducerRecord class
        //ProducerRecord<String, String> producerRecord2 = new ProducerRecord<>(kafkaTopic.name(), "key", message);

        // send data - asynchronous
        kafkaProducer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                //executes every time a record successfully sent or an exception is thrown
                if (e == null) {
                    // record was sent successfully
                    log.info("Received new metadata \n" +
                            "Topic: " + recordMetadata.topic() + "\n" +
                            "Partition: " + recordMetadata.partition() + "\n" +
                            "Offset: " + recordMetadata.offset() + "\n" +
                            "Timestamp: " + recordMetadata.timestamp());
                } else {
                    log.error("Error while producing - {} ", e.getMessage());
                }
            }
        });
        // flush and close the producer
        kafkaProducer.close();
    }
}
