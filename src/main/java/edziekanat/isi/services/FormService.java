package edziekanat.isi.services;

import edziekanat.isi.dto.*;
import edziekanat.isi.exceptions.*;
import edziekanat.isi.misc.CustomUserDetails;
import edziekanat.isi.misc.FormField;
import edziekanat.isi.misc.FormFilledField;
import edziekanat.isi.misc.SentFormStatusE;
import edziekanat.isi.models.FormTemplate;
import edziekanat.isi.models.SentForm;
import edziekanat.isi.models.SentFormStatus;
import edziekanat.isi.models.User;
import edziekanat.isi.repositories.FormTemplateRepository;
import edziekanat.isi.repositories.SentFormRepository;
import edziekanat.isi.repositories.SentFormStatusRepository;
import edziekanat.isi.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @PreAuthorize("hasAnyRole('ADMIN','WORKER')")
    public Page<SentFormDTO> getAllSentForms(Pageable pageable) {
        return sentFormRepository.findAll(pageable).map(SentFormDTO::new);
    }

    @PreAuthorize("hasAnyRole('ADMIN','WORKER')")
    public void createFormTemplate(FormTemplateCreationRequest formTemplateCreationRequest) {
        String title = formTemplateCreationRequest.getTitle();
        List<FormField> formFields = formTemplateCreationRequest.getFormFields();
        LocalDateTime createdAt = LocalDateTime.now();

        FormTemplate newFormTemplate = new FormTemplate(createdAt, title, formFields);
        formTemplateRepository.save(newFormTemplate);
    }

    public Page<StudentSentFormDTO> getFormTemplates(Pageable pageable, Authentication authentication) {
        CustomUserDetails userDetails = CheckAuth(authentication);

        Map<Integer, Integer> statusByTemplateId = sentFormRepository.findByUserId(userDetails.getUserId())
                .stream()
                .collect(Collectors.toMap(
                        sf -> sf.getFormTemplate().getId(),
                        sf -> sf.getStatus().getId()
                ));

        return formTemplateRepository.findAll(pageable)
                .map(template -> new StudentSentFormDTO(
                        template.getId(),
                        statusByTemplateId.getOrDefault(
                                template.getId(),
                                SentFormStatusE.NOT_SENT.getId()
                        ),
                        template.getTitle()
                ));
    }

    public FormTemplateDTO getTemplate(Integer formTemplateId, Long userId) {
        Optional<FormTemplate> formTemplate = formTemplateRepository.findById(formTemplateId);
        if(formTemplate.isEmpty()) throw new FormTemplateNotFoundException(formTemplateId.toString());

        List<FormFilledField> formFilledFields = new ArrayList<>();
        Optional<SentForm> sentForm = sentFormRepository.findByUserIdAndFormTemplateId(userId, formTemplateId);
        Integer statusId = SentFormStatusE.NOT_SENT.getId();
        String response = "";
        if(sentForm.isPresent()) {
            formFilledFields = sentForm.get().getFormData();
            statusId = sentForm.get().getStatus().getId();
            response = sentForm.get().getResponse();
        }

        FormTemplateDTO formTemplateDTO = new FormTemplateDTO(formTemplate.get());
        formTemplateDTO.setFormFilledFields(formFilledFields);
        formTemplateDTO.setStatusId(statusId);
        formTemplateDTO.setResponse(response);

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
            if(sentForm.get().getStatus().getId() != SentFormStatusE.IN_NEED_OF_UPDATE.getId()) throw new UnauthorizedException(); //ig this counts as unauthorized? I think?
            newSentForm = new SentForm(sentForm.get().getId(), user.get(), formTemplate.get(), status.get(), request.getFormData());
        }
        else {
            newSentForm = new SentForm(user.get(), formTemplate.get(), status.get(), request.getFormData());
        }


        sentFormRepository.save(newSentForm);
    }

    @PreAuthorize("hasAnyRole('ADMIN','WORKER')")
    @Transactional
    public void updateSentFormStatus(UpdateSentFormRequest request, Authentication authentication) {
        CustomUserDetails userDetails = CheckAuth(authentication);

        SentForm sentForm = sentFormRepository.findById(request.getSentFormId()).orElseThrow(SentFormNotFoundException::new);
        SentFormStatus status = sentFormStatusRepository.findById(request.getNewStatusId()).orElseThrow(SentFormStatusNotFoundException::new);

        sentForm.setResponse(request.getResponse());
        sentForm.setStatus(status);

        sentFormRepository.save(sentForm);
    }

    private CustomUserDetails CheckAuth(Authentication authentication) {
        System.out.println(authentication);

        if(authentication == null) throw new UnauthorizedException();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if(userDetails == null) throw new UnauthorizedException();

        return userDetails;
    }
}
