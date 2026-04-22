package edziekanat.isi.controllers;

import edziekanat.isi.models.LoginData;
import edziekanat.isi.models.User;
import edziekanat.isi.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.mindrot.jbcrypt.BCrypt;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public User login(HttpSession session, HttpServletResponse response, HttpServletRequest request, @Validated @RequestBody LoginData loginData) {
        User userSession = userRepository.getFromSession(session);
        User user = userRepository.credentialsCorrect(loginData);

        if(userSession == null && user == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
        else if (userSession != null && user == null)  return userSession;
        else if (userSession == null && user != null){
            session.setAttribute("permissions", user.getUserPermissions());
            session.setAttribute("id", user.getId());
            return user;
        }
        else {
            session.invalidate();
            session = request.getSession(true);
            session.setAttribute("permissions", user.getUserPermissions());
            session.setAttribute("id", user.getId());
            return user;
        }
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        try {
            session.invalidate();
        } catch (IllegalStateException ignored) {

        }
    }
}
