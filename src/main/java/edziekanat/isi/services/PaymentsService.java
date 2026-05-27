package edziekanat.isi.services;

import edziekanat.isi.models.Payments;
import edziekanat.isi.models.User;
import edziekanat.isi.repositories.PaymentsRepository;
import edziekanat.isi.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentsService {
    private final PaymentsRepository paymentsRepository;

    public PaymentsService(PaymentsRepository paymentsRepository){
        this.paymentsRepository = paymentsRepository;
    }

    public Payments crateNewPayment(long userId, float amount){
        User user = UserRepository.findById(userId).orElseThrow();
        Payments payment = new Payments();

        payment.setUserId(user);
        payment.setAmount(amount);
        payment.setStatus("Pending");

        return paymentsRepository.save(payment);
    }

    public boolean updatePayment(){

        return false;
    }

}