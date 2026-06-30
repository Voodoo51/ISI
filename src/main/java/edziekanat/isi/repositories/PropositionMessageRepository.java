package edziekanat.isi.repositories;

import edziekanat.isi.models.PropositionMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PropositionMessageRepository extends JpaRepository<PropositionMessage, Long> {
    Page<PropositionMessage> findAllByPropositionIdOrderByCreatedAtDesc(
            Long propositionId,
            Pageable pageable
    );}
