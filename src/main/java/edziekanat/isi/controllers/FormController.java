package edziekanat.isi.controllers;

import edziekanat.isi.dto.*;
import edziekanat.isi.services.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/form")
public class FormController {
    @Autowired
    private FormService formService;

    //Change the name of this DTO class!!!!
    @GetMapping("/sent")
    public ResponseEntity<Page<StudentSentFormDTO>>getAllForms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );

        return ResponseEntity.ok(formService.getFormTemplates(pageable, authentication));
        //return ResponseEntity.ok().build();
        //return ResponseEntity.ok(formService.getSentForms(userId));
    }

    @GetMapping("/sent/all")
    public ResponseEntity<Page<SentFormDTO>> getAllForms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );

        return ResponseEntity.ok(formService.getAllSentForms(pageable));
    }

    @PutMapping("/sent/update")
    public ResponseEntity<Void> updateSentForm(
            @RequestBody UpdateSentFormRequest request,
            Authentication authentication) {
        formService.updateSentFormStatus(request, authentication);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createFormTemplate(@Validated @RequestBody FormTemplateCreationRequest formTemplateCreationRequest) {
        formService.createFormTemplate(formTemplateCreationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendForm(@Validated @RequestBody SendFormRequest request, Authentication authentication) {
        formService.sendForm(request, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/template")
    public ResponseEntity<FormTemplateDTO> getTemplate(@RequestParam Integer formTemplateId, @RequestParam Long userId) {
        FormTemplateDTO formTemplateDTO = formService.getTemplate(formTemplateId, userId);
        return ResponseEntity.ok(formTemplateDTO);
    }

    /*
    @PutMapping("/add")
    public void addTemplate(HttpSession session, HttpServletResponse response, Form form) {
        User.UserPermissions userPermissions = (User.UserPermissions) session.getAttribute("permissions");
        if(userPermissions == User.UserPermissions.Admin || userPermissions == User.UserPermissions.Worker) {
            if(!formRepository.insertFormTemplate(form)) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } else response.setStatus(HttpStatus.FORBIDDEN.value());
    }

    @DeleteMapping("/delete")
    public void deleteTemplate(HttpSession session, HttpServletResponse response, Form form) {
        User.UserPermissions userPermissions = (User.UserPermissions) session.getAttribute("permissions");
        if(userPermissions == User.UserPermissions.Admin || userPermissions == User.UserPermissions.Worker) {
            if(!formRepository.deleteFormTemplate(form)) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } else response.setStatus(HttpStatus.FORBIDDEN.value());
    }
     */
}
