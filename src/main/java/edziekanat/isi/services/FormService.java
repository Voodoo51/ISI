package edziekanat.isi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import edziekanat.isi.dto.FormTemplateCreationRequest;
import edziekanat.isi.dto.FormTemplateListDTO;
import edziekanat.isi.dto.FormTemplateDTO;
import edziekanat.isi.dto.SendFormRequest;
import edziekanat.isi.dto.SentFormDTO;
import edziekanat.isi.exceptions.FormTemplateNotFoundException;
import edziekanat.isi.exceptions.InternalServerErrorException;
import edziekanat.isi.exceptions.UnauthorizedException;
import edziekanat.isi.exceptions.UserNotFoundException;
import edziekanat.isi.misc.CustomUserDetails;
import edziekanat.isi.misc.FormField;
import edziekanat.isi.misc.FormFilledField;
import edziekanat.isi.models.FormTemplate;
import edziekanat.isi.models.SentForm;
import edziekanat.isi.models.SentFormStatus;
import edziekanat.isi.models.User;
import edziekanat.isi.repositories.FormTemplateRepository;
import edziekanat.isi.repositories.SentFormRepository;
import edziekanat.isi.repositories.SentFormStatusRepository;
import edziekanat.isi.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FormService {
    @Autowired
    private SentFormRepository sentFormRepository;
    @Autowired
    private SentFormStatusRepository sentFormStatusRepository;
    @Autowired
    private FormTemplateRepository formTemplateRepository;
    @Autowired
    private UserRepository userRepository;

    public List<SentFormDTO> getSentForms(Long userId) {
        if(userId == null) return new ArrayList<>();

        List<SentForm> sentForms = sentFormRepository.findByUserId(userId);
        return sentForms.stream()
                .map(SentFormDTO::new)
                .collect(Collectors.toList());
    }

    public void createFormTemplate(FormTemplateCreationRequest formTemplateCreationRequest) {
        String title = formTemplateCreationRequest.getTitle();
        List<FormField> formFields = formTemplateCreationRequest.getFormFields();
        LocalDateTime createdAt = LocalDateTime.now();

        FormTemplate newFormTemplate = new FormTemplate(createdAt, title, formFields);
        formTemplateRepository.save(newFormTemplate);
    }

    public List<FormTemplateListDTO> getFormTemplates(Authentication authentication) {
        CustomUserDetails userDetails = CheckAuth(authentication);

        List<SentForm> sentForms =  sentFormRepository.findByUserId(userDetails.getUserId());
        return formTemplateRepository.findAll()
                .stream()
                .map(template ->  {
                    Optional<SentForm> sentForm = sentForms.stream()
                                    .filter(sf -> sf.getFormTemplate().getId() == template.getId())
                                    .findFirst();

                    Integer statusId = 3;
                    if(sentForm.isPresent()) statusId = sentForm.get().getStatus().getId();

                    return new FormTemplateListDTO(template.getId(), statusId, template.getTitle());
                }).toList();
//                .map(template -> new FormTemplateListDTO(template.getId(), template.getTitle())).toList();
    }

    public FormTemplateDTO getTemplate(Integer templateId, Authentication authentication) {
        CustomUserDetails userDetails = CheckAuth(authentication);

        Optional<FormTemplate> formTemplate = formTemplateRepository.findById(templateId);
        if(formTemplate.isEmpty()) throw new FormTemplateNotFoundException(templateId.toString());

        List<FormFilledField> formFilledFields = new ArrayList<>();
        Optional<SentForm> sentForm = sentFormRepository.findByUserIdAndFormTemplateId(userDetails.getUserId(), templateId);
        Integer statusId = 3;
        if(sentForm.isPresent()) {
            formFilledFields = sentForm.get().getFormData();
            statusId = sentForm.get().getStatus().getId();
        }

        FormTemplateDTO formTemplateDTO = new FormTemplateDTO(formTemplate.get());
        formTemplateDTO.setFormFilledFields(formFilledFields);
        formTemplateDTO.setStatusId(statusId);

        return formTemplateDTO;
    }

    public void sendForm(SendFormRequest request, Authentication authentication) {
        CustomUserDetails userDetails = CheckAuth(authentication);

        Optional<SentFormStatus> status = sentFormStatusRepository.findById(1);
        if(status.isEmpty()) throw new InternalServerErrorException("Sent form status error. Status should exist.");

        Optional<User> user = userRepository.findById(userDetails.getUserId());
        if(user.isEmpty()) throw new UserNotFoundException();

        Optional<FormTemplate> formTemplate = formTemplateRepository.findById(request.getFormTemplateId());
        if(formTemplate.isEmpty()) throw new InternalServerErrorException("Template not found, possibly removed when sending form data.");

        Optional<SentForm> sentForm = sentFormRepository.findByUserIdAndFormTemplateId(user.get().getId(), request.getFormTemplateId());

        SentForm newSentForm;
        if(sentForm.isPresent()) {
            newSentForm = new SentForm(sentForm.get().getId(), user.get(), formTemplate.get(), status.get(), request.getFormData());
        }
        else {
            newSentForm = new SentForm(user.get(), formTemplate.get(), status.get(), request.getFormData());
        }

        sentFormRepository.save(newSentForm);
    }

    private CustomUserDetails CheckAuth(Authentication authentication) {
        if(authentication == null) throw new UnauthorizedException();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if(userDetails == null) throw new UnauthorizedException();

        return userDetails;
    }
}
