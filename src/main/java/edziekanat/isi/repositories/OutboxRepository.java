package edziekanat.isi.repositories;

import edziekanat.isi.models.OutboxEvent;
import edziekanat.isi.models.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findBySentFalse();
    Optional<OutboxEvent> findByPaymentId(Long paymentId);
}
