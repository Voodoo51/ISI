package edziekanat.isi.repositories;

import edziekanat.isi.models.SentFormStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SentFormStatusRepository extends JpaRepository<SentFormStatus, Integer> {
    Optional<SentFormStatus> findById(Integer id);
}
