package edziekanat.isi.repositories;

import edziekanat.isi.models.PropositionFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropositionFileRepository extends JpaRepository<PropositionFile,Long> {
}
