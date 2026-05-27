package edziekanat.isi.services;

import edziekanat.isi.models.Payments;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "topic1";
    private final KafkaTemplate<String,String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        System.out.println("Sending to Kafka: " + message);
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Message sent: " + message);
    }

    public void sendRetry(String message) {

        kafkaTemplate.send("retry_topic", message);
        System.out.println("Sent to retry_topic: " + message);
    }

}