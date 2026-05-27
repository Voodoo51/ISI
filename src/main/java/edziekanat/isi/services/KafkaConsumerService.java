package edziekanat.isi.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
/*
    @KafkaListener(topics = "topic1", groupId = "group_id")
    public void consume(String message) {
        System.out.println("Message received: " + message);
    }
    */

    @KafkaListener(topics = "topic1", groupId = "group_id")
    public void consume(String message) {
        System.out.println("Message received: /n" + message);
        //set payment as PROCESSING
        //call payment provider
        //if it fails add 1 to count and sent to retry_topic
        //if succes update database as PAID
    }

    @KafkaListener(topics = "retry_topic", groupId = "group_id")//idk czy dac więcej grup, raczej nie
    public void consumeAgain(String message) {
        System.out.println("Message received: /n" + message);
        //if count>3 send to failed_topic
        //call payment provider
        //if it fails add 1 to count and sent to retry_topic
        //if succes update database as PAID
    }

}