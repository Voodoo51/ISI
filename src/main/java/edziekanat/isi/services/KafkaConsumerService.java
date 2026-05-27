package edziekanat.isi.services;

import edziekanat.isi.models.Payments;
import edziekanat.isi.repositories.PaymentsRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    PaymentsRepository paymentsRepository;
/*
    @KafkaListener(topics = "topic1", groupId = "group_id")
    public void consume(String message) {
        System.out.println("Message received: " + message);
    }
    */

    @KafkaListener(topics = "topic1", groupId = "group_id")
    public void consume(String message) {
        System.out.println("Message received: /n" + message);

        String paymentStringId = message.split(",")[0];
        Long paymentId = Long.parseLong(paymentStringId);

        Payments payments = paymentsRepository.getById(paymentId);
        payments.setStatus("PROCESSING");
        paymentsRepository.save(payments);

        //call payment provider
        Boolean processingStatus= true; //hardcoded powinna być zwrotka od payment provider
        if(processingStatus){

            payments.setStatus("PAID");
            paymentsRepository.save(payments);
        }else{
            String processingCountString = message.split(",")[6];
            int processingCount = Integer.getInteger(processingCountString)+1;

        }
        //if it fails add 1 to count and sent to retry_topic
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