package edziekanat.isi.services;

import edziekanat.isi.models.OutboxEvent;
import edziekanat.isi.models.Payments;
import edziekanat.isi.repositories.OutboxRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxProcessor {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String,String> kafkaTemplate;

    public OutboxProcessor(OutboxRepository outboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }



    @Scheduled(fixedRate = 5000)
    public void processPaymentOutbox() {

        List<OutboxEvent> events = outboxRepository.findBySentFalse();

        for (OutboxEvent event : events) {

            try {
                kafkaTemplate.send(event.getTopic(), event.getPayload());
                event.setsent(true);
                outboxRepository.save(event);
            } catch (Exception e) {
                System.out.println("Kafka failed with "+ event.getPayload()+ " , will retry later");
            }
        }
    }

}
