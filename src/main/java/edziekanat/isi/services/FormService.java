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
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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

    public Page<SentFormDTO> getAllSentForms(Integer statusId, Pageable pageable) {
        return sentFormRepository.findAllByStatus(statusId, pageable).map(SentFormDTO::new);
    }

    public void createFormTemplate(FormTemplateCreationRequest formTemplateCreationRequest, MultipartFile pdfFile) {
        String title = formTemplateCreationRequest.getTitle();
        List<FormField> formFields = formTemplateCreationRequest.getFormFields();
        LocalDateTime createdAt = LocalDateTime.now();

        try {
            FormTemplate newFormTemplate = new FormTemplate(createdAt, title, formFields, pdfFile.getBytes());
            formTemplateRepository.save(newFormTemplate);
        }
        catch (IOException e ) {
            throw new FileErrorException();
        }
    }

    public Page<SentFormDTO> searchSentForms(
            String query,
            Integer statusId,
            Pageable pageable,
            Authentication authentication
    ) {

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        boolean privileged = user.getRole().getName().equals("admin") ||user.getRole().getName().equals("worker");


        if (privileged) {
            return sentFormRepository
                    .search(query, statusId, pageable)
                    .map(SentFormDTO::new);
        }


        return sentFormRepository
                .searchUserForms(user.getId(), query, statusId, pageable)
                .map(SentFormDTO::new);
    }

    public Page<StudentSentFormDTO> getFormTemplates(
            Integer statusId,
            Pageable pageable,
            Authentication authentication
    ) {

        CustomUserDetails userDetails = CheckAuth(authentication);


        Map<Integer, Integer> statusByTemplateId =
                sentFormRepository
                        .findByUserIdAndOptionalStatus(
                                userDetails.getUserId(),
                                statusId
                        )
                        .stream()
                        .collect(Collectors.toMap(
                                sf -> sf.getFormTemplate().getId(),
                                sf -> sf.getStatus().getId()
                        ));


        return formTemplateRepository
                .findAll(pageable)
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
        validateFormData(formTemplate.get(), request.getFormData());

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

    private void validateFormData(FormTemplate template, List<FormFilledField> submittedFields) {

        Map<Integer, FormFilledField> submitted = submittedFields.stream()
                .collect(Collectors.toMap(FormFilledField::getId, Function.identity()));

        List<FormField> templateFields = template.getFormFields();

        // Ensure the same number of fields
        if (submitted.size() != templateFields.size()) {
            throw new BadRequestException("Submitted fields do not match the template.");
        }

        for (FormField field : templateFields) {

            FormFilledField submittedField = submitted.get(field.getId());

            if (submittedField == null) {
                throw new BadRequestException("Missing field: " + field.getLabel());
            }

            String value = submittedField.getValue() == null
                    ? ""
                    : submittedField.getValue().trim();

            if (value.isEmpty()) {
                throw new BadRequestException("Field '" + field.getLabel() + "' cannot be empty.");
            }

            switch (field.getType()) {

                case "phoneNumber":
                    if (!value.replaceAll("\\s", "").matches("^\\+?[0-9]{9,15}$"))
                        throw new BadRequestException("Invalid phone number.");
                    break;

                case "pesel":
                    if (!value.matches("^\\d{11}$"))
                        throw new BadRequestException("Invalid PESEL.");
                    break;

                case "email":
                    if (!value.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
                        throw new BadRequestException("Invalid email.");
                    break;

                case "album":
                    if (!value.matches("^\\d+$"))
                        throw new BadRequestException("Invalid album number.");
                    break;

                case "date":
                    if (!isValidDate(value))
                        throw new BadRequestException("Invalid date.");
                    break;

                default:
                    break;
            }
        }
    }

    private boolean isValidDate(String value) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd.MM.yyyy")
                        .withResolverStyle(ResolverStyle.STRICT);
        try {
            LocalDate.parse(value, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
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

    public FormTemplateFileDTO getTemplatePdf(Integer templateId) {
        FormTemplate formTemplate = formTemplateRepository.findById(templateId).orElseThrow(() -> {
            throw new FormTemplateNotFoundException(templateId.toString());}
        );

        return new FormTemplateFileDTO(formTemplate);
    }

    private CustomUserDetails CheckAuth(Authentication authentication) {
        System.out.println(authentication);

        if(authentication == null) throw new UnauthorizedException();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if(userDetails == null) throw new UnauthorizedException();

        return userDetails;
    }
}
