package edziekanat.isi.services;

import edziekanat.isi.exceptions.UserNotFoundException;
import edziekanat.isi.models.OutboxEvent;
import edziekanat.isi.models.Payments;
import edziekanat.isi.models.User;
import edziekanat.isi.repositories.OutboxRepository;
import edziekanat.isi.repositories.PaymentsRepository;
import edziekanat.isi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PaymentsService {
    @Autowired
    private PaymentsRepository paymentsRepository;
    @Autowired
    private UserRepository userRepository;
    private OutboxRepository outboxRepository;
    private final KafkaProducerService kafkaService;
    private final int retryCount = 0;

    public PaymentsService(PaymentsRepository paymentsRepository, OutboxRepository outboxRepository, UserRepository usersRepository, OutboxRepository outboxRepository1, KafkaProducerService kafkaService){
        this.paymentsRepository = paymentsRepository;
        this.userRepository = usersRepository;
        this.outboxRepository = outboxRepository1;
        this.kafkaService = kafkaService;
    }

    @Transactional
    public Payments crateNewPayment(Long userId, Long amount){
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new UserNotFoundException();

        Payments payment = new Payments();
        payment.setUser(user.get());
        payment.setAmount(amount);
        payment.setStatus("Pending");

        OutboxEvent event = new OutboxEvent();
        event.setTopic("topic1");
        event.setPayload(payment.getId() + "," +
                payment.getAmount() + "," +
                payment.getStatus()
        );

        outboxRepository.save(event);

        return paymentsRepository.save(payment);
    }

    public boolean updatePayment(){
        return false;
    }

}