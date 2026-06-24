package edziekanat.isi.repositories;

import edziekanat.isi.models.Payment;
import edziekanat.isi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByUserId(Long userId);
    Optional<Payment> findByOrderId(String orderId);
}
