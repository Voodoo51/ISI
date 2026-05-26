package edziekanat.isi.services;

import edziekanat.isi.dto.SentFormDTO;
import edziekanat.isi.models.SentForm;
import edziekanat.isi.repositories.SentFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormService {
    @Autowired
    private SentFormRepository sentFormRepository;

    public List<SentFormDTO> getSentForms(Long userId) {
        if(userId == null) return new ArrayList<>();

        List<SentForm> sentForms = sentFormRepository.findByUserId(userId);
        return sentForms.stream()
                .map(SentFormDTO::new)
                .collect(Collectors.toList());
    }
}
