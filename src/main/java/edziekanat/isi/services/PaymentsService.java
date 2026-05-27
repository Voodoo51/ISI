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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PaymentsService {
    private PaymentsRepository paymentsRepository;
    private UserRepository userRepository;
    private OutboxRepository outboxRepository;
    private final KafkaProducerService kafkaService;
    private final int retryCount = 0;

    public PaymentsService(PaymentsRepository paymentsRepository, OutboxRepository outboxRepository, UserRepository usersRepository, KafkaProducerService kafkaService){
        this.paymentsRepository = paymentsRepository;
        this.userRepository = usersRepository;
        this.outboxRepository = outboxRepository;
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
        Payments savedPayment= paymentsRepository.save(payment);

        OutboxEvent event = new OutboxEvent();
        event.setTopic("topic1");
        event.setPaymentId(savedPayment.getId());
        event.setPayload(savedPayment.getId() + "," +
                savedPayment.getAmount() + "," +
                savedPayment.getStatus()
        );

        outboxRepository.save(event);

        return savedPayment;
    }


}