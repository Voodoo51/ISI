package edziekanat.isi.controllers;

import edziekanat.isi.dto.FormTemplateCreationRequest;
import edziekanat.isi.dto.FormTemplateListDTO;
import edziekanat.isi.dto.FormTemplateDTO;
import edziekanat.isi.dto.SendFormRequest;
import edziekanat.isi.dto.SentFormDTO;
import edziekanat.isi.repositories.FormRepositoryDeprecated;
import edziekanat.isi.services.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/form")
public class FormController {
    @Autowired
    private FormService formService;

    //Kafka
    @PostMapping("/sent/{userId}")
    public ResponseEntity<Void> getAllForms(@RequestBody SendFormRequest request) {
        return ResponseEntity.ok().build();
        //return ResponseEntity.ok(formService.getSentForms(userId));
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

    @GetMapping("/templates")
    public ResponseEntity<List<FormTemplateListDTO>> getTemplates(Authentication authentication) {
        return ResponseEntity.ok(formService.getFormTemplates(authentication));
    }

    @GetMapping("/templates/{id}")
    public ResponseEntity<FormTemplateDTO> getTemplate(@PathVariable Integer id, Authentication authentication) {
        FormTemplateDTO formTemplateDTO = formService.getTemplate(id, authentication);
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
