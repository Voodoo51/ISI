package edziekanat.isi.services;

import edziekanat.isi.models.Payments;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "topic1";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        System.out.println("Sending to Kafka: " + message);
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Message sent: " + message);
    }

    //nowe
    public void sendPayment(Payments paymentJson) {
        //kafkaTemplate.send("payments-topic", paymentId);
        kafkaTemplate.send("payments", paymentJson.getId().toString());//na razie jest tylko id przesyłane
        System.out.println("Payment sent: " + paymentJson);
    }
}