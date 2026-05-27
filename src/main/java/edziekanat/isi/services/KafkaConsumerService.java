package edziekanat.isi.services;

import edziekanat.isi.models.OutboxEvent;
import edziekanat.isi.models.Payments;
import edziekanat.isi.repositories.OutboxRepository;
import edziekanat.isi.repositories.PaymentsRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final PaymentsRepository paymentsRepository;
    private final OutboxRepository outboxRepository;
    private final KafkaProducerService kafkaProducerService;

    public KafkaConsumerService(PaymentsRepository paymentsRepository, OutboxRepository outboxRepository, KafkaProducerService kafkaProducerService) {
        this.paymentsRepository = paymentsRepository;
        this.outboxRepository = outboxRepository;
        this.kafkaProducerService = kafkaProducerService;
    }
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
        Boolean processingEndStatus= true; //hardcoded powinna być zwrotka od payment provider
        if(processingEndStatus){

            payments.setStatus("PAID");
            paymentsRepository.save(payments);

        }else{
            OutboxEvent event =  outboxRepository.findByPaymentId(paymentId).orElseThrow();
//
//            int newCount = event.getRetryCount()+1;
//            event.setRetryCount(newCount);

            kafkaProducerService.sendRetry(event.getPayload());
            //outboxRepository.save(event);
            //czy te dwie rzeczy tak sa dobrze czy trzeba transaction
        }

    }

    @KafkaListener(topics = "retry_topic", groupId = "group_id")//idk czy dac więcej grup, raczej nie
    public void consumeAgain(String message) {
        System.out.println("Message received: /n" + message);

        Long paymentId = Long.parseLong(message.split(",")[0]);
        OutboxEvent event =  outboxRepository.findByPaymentId(paymentId).orElseThrow();

        int retryCount = event.getRetryCount();

        if(retryCount > 3){

            Payments payment = paymentsRepository
                    .findById(paymentId)
                    .orElseThrow();

            payment.setStatus("FAILED");
            paymentsRepository.save(payment);

            return;
        }


        //call payment provider
        Boolean processingEndStatus= true; //hardcoded powinna być zwrotka od payment provider
        if(processingEndStatus){

            Payments payment = paymentsRepository
                    .findById(paymentId)
                    .orElseThrow();
            payment.setStatus("PAID");
            paymentsRepository.save(payment);

        }else{

            int newCount = event.getRetryCount()+1;
            event.setRetryCount(newCount);
            kafkaProducerService.sendRetry(event.getPayload());
            outboxRepository.save(event);
            //czy te dwie rzeczy tak sa dobrze czy trzeba transaction
        }
        // retry processing
        //if count>3 send to failed_topic
        //call payment provider
        //if it fails add 1 to count and sent to retry_topic
        //if succes update database as PAID
    }
//mozna wycagnac funkcje jakas mayby
}