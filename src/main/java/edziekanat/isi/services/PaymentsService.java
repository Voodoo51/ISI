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

    public PaymentsService(PaymentsRepository paymentsRepository, UserRepository usersRepository){
        this.paymentsRepository = paymentsRepository;
        this.userRepository = usersRepository;
    }

    public Payments crateNewPayment(Long userId, Long amount){
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new UserNotFoundException();

        Payments payment = new Payments();
        payment.setUser(user.get());
        payment.setAmount(amount);
        payment.setStatus("Pending");

        return paymentsRepository.save(payment);
    }

    public boolean updatePayment(){
        return false;
    }

}