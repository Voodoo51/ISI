package edziekanat.isi.repositories;

import edziekanat.isi.models.Payment;
import edziekanat.isi.models.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Integer> {
    Optional<PaymentStatus> findByName(String name);
}
