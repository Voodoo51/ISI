package edziekanat.isi.repositories;

import edziekanat.isi.models.FormTemplate;
import edziekanat.isi.models.SentForm;
import edziekanat.isi.models.SentFormStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SentFormRepository extends JpaRepository<SentForm, Long>{
    List<SentForm> findByUserId(Long userId);
    Optional<SentForm> findByUserIdAndFormTemplateId(Long userId, Integer templateId);
}
