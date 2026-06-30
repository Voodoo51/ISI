package edziekanat.isi.controllers;

import edziekanat.isi.dto.RegisterRequest;
import edziekanat.isi.misc.LoginData;
import edziekanat.isi.dto.UserPublicData;
import edziekanat.isi.models.User;
import edziekanat.isi.repositories.UserRepository;
import edziekanat.isi.services.AuthorizationService;
import edziekanat.isi.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN','WORKER')")
    @PostMapping("/register")
    public ResponseEntity<UserPublicData> register(@Validated @RequestBody RegisterRequest registerRequest, Authentication authentication) {
        UserPublicData userPublicData = userService.register(registerRequest, authentication);
        return ResponseEntity.ok(userPublicData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPublicData> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<UserPublicData>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "surname")
        );

        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/all/search")
    public ResponseEntity<Page<UserPublicData>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "surname")
        );

        return ResponseEntity.ok(userService.searchUsers(query, pageable));
    }

    /*
    @PostMapping("/login")
    public ResponseEntity<UserPublicData> login(@Validated @RequestBody LoginData loginData) {
        UserPublicData userPublicData = authorizationService.login(loginData);

        return ResponseEntity.ok(userPublicData);

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
    }
         */

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        try {
            System.out.println("TEST");
            session.invalidate();
        } catch (IllegalStateException ignored) {

        }
    }
}
