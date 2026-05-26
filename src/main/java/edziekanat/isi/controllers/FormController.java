package edziekanat.isi.controllers;

import edziekanat.isi.dto.SentFormDTO;
import edziekanat.isi.repositories.FormRepositoryDeprecated;
import edziekanat.isi.services.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/form")
public class FormController {
    @Autowired
    private FormService formService;

    @PostMapping("/sent/{userId}")
    public ResponseEntity<List<SentFormDTO>> getAllForms(@PathVariable Long userId) {
        return ResponseEntity.ok(formService.getSentForms(userId));
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
