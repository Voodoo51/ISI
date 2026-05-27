package edziekanat.isi.services;

import edziekanat.isi.exceptions.UserNotFoundException;
import edziekanat.isi.models.Payments;
import edziekanat.isi.models.User;
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
    private final KafkaProducerService kafkaService;

    public PaymentsService(PaymentsRepository paymentsRepository, UserRepository usersRepository, KafkaProducerService kafkaService){
        this.paymentsRepository = paymentsRepository;
        this.userRepository = usersRepository;
        this.kafkaService = kafkaService;
    }

    public Payments crateNewPayment(Long userId, Long amount){
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new UserNotFoundException();

        Payments payment = new Payments();
        payment.setUser(user.get());
        payment.setAmount(amount);
        payment.setStatus("Pending");

        kafkaService.sendPayment(payment ); //id czy to tutaj czy w kontrolerze
        return paymentsRepository.save(payment);
    }

    public boolean updatePayment(){
        return false;
    }

}