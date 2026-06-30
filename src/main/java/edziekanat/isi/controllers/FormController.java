package edziekanat.isi.controllers;

import edziekanat.isi.dto.*;
import edziekanat.isi.services.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/form")
public class FormController {
    @Autowired
    private FormService formService;

    //Change the name of this DTO class!!!!
    @GetMapping("/sent")
    public ResponseEntity<Page<StudentSentFormDTO>>getAllForms(
            @RequestParam(required = false) Integer statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );

        return ResponseEntity.ok(formService.getFormTemplates(statusId, pageable, authentication));
        //return ResponseEntity.ok().build();
        //return ResponseEntity.ok(formService.getSentForms(userId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','WORKER')")
    @GetMapping("/sent/all")
    public ResponseEntity<Page<SentFormDTO>> getAllForms(
            @RequestParam(required = false) Integer statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size
        );

        return ResponseEntity.ok(formService.getAllSentForms(statusId, pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN','WORKER','STUDENT')")
    @GetMapping("/sent/search")
    public ResponseEntity<Page<SentFormDTO>> searchForms(
            @RequestParam String query,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "sentAt")
        );


        return ResponseEntity.ok(
                formService.searchSentForms(
                        query,
                        statusId,
                        pageable,
                        authentication
                )
        );
    }

    @PutMapping("/sent/update")
    public ResponseEntity<Void> updateSentForm(
            @RequestBody UpdateSentFormRequest request,
            Authentication authentication) {
        formService.updateSentFormStatus(request, authentication);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','WORKER')")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createFormTemplate(
            @RequestPart("request") FormTemplateCreationRequest formTemplateCreationRequest,
            @RequestPart("file") MultipartFile pdfFile) {
        formService.createFormTemplate(formTemplateCreationRequest, pdfFile);
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

    @GetMapping("template/file/{templateId}")
    public ResponseEntity<byte[]> getTemplateFile(@PathVariable Integer templateId) {
        FormTemplateFileDTO formTemplateFileDTO = formService.getTemplatePdf(templateId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + formTemplateFileDTO.getTitle() + ".pdf")
                .body(formTemplateFileDTO.getPdfFile());
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
