package edziekanat.isi.repositories;

import edziekanat.isi.models.Payment;
import edziekanat.isi.models.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Integer> {
}
