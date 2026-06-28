package edziekanat.isi.repositories;

import edziekanat.isi.models.Proposition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropositionRepository extends JpaRepository<Proposition,Long> {
    List<Proposition> findAllByUserId(Long userId);
}
