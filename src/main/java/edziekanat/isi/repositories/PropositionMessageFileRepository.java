package edziekanat.isi.repositories;

import edziekanat.isi.models.PropositionMessage;
import edziekanat.isi.models.PropositionMessageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropositionMessageFileRepository extends JpaRepository<PropositionMessageFile, Long> {
    Optional<PropositionMessageFile> findById(Long fileId);
}
