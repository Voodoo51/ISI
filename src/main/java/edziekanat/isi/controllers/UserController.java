package edziekanat.isi.controllers;

import edziekanat.isi.misc.LoginData;
import edziekanat.isi.dto.UserPublicData;
import edziekanat.isi.services.AuthorizationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<UserPublicData> login(@Validated @RequestBody LoginData loginData) {
        UserPublicData userPublicData = authorizationService.login(loginData);

        return ResponseEntity.ok(userPublicData);
        /*
        //HttpSession session, HttpServletResponse response, HttpServletRequest request
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
         */
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        try {
            session.invalidate();
        } catch (IllegalStateException ignored) {

        }
    }
}
