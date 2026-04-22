package edziekanat.isi.controllers;

import edziekanat.isi.models.Form;
import edziekanat.isi.models.User;
import edziekanat.isi.repositories.FormRepository;
import edziekanat.isi.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/form")
public class FormController {
    private final FormRepository formRepository;

    public FormController(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

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
}
