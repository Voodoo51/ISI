package edziekanat.isi.controllers;

import edziekanat.isi.dto.LoginRequest;
import edziekanat.isi.dto.UserPublicData;
import edziekanat.isi.exceptions.UnauthorizedException;
import edziekanat.isi.misc.CustomUserDetails;
import edziekanat.isi.misc.LoginData;
import edziekanat.isi.models.User;
import edziekanat.isi.models.UserRole;
import edziekanat.isi.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.AuthProvider;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    @Autowired
    private AuthorizationService authorizationService;
    //public UserPublicData(long id, String email, String name, String surname, UserRole role) {


    @GetMapping("/github/oauth")
    public RedirectView githubOAUTH(Authentication authentication) {
        return new RedirectView(authorizationService.redirectGithub());
    }

    @GetMapping("github/callback")
    public ResponseEntity<UserPublicData> githubCallback(@RequestParam(required = true) String code ) {
        return null;
    }

    @GetMapping("/me")
    public UserPublicData me(Authentication authentication) {
        try
        {
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            return new UserPublicData(user.getUser());
        } catch (NullPointerException e)
        {
            throw new UnauthorizedException();
        }
    }
    /*
    @PostMapping("/login")
    public ResponseEntity<UserPublicData> login(@Validated @RequestBody LoginRequest loginRequest) {
        UserPublicData userPublicData = authorizationService.login(loginRequest);

        if(userPublicData == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(userPublicData);
    }*/
}
