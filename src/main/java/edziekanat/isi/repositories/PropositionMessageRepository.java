package edziekanat.isi.repositories;

import edziekanat.isi.models.PropositionMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropositionMessageRepository extends JpaRepository<PropositionMessage, Long> {
    List<PropositionMessage> findAllByPropositionId(Long propositionId);
}
