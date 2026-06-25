package edziekanat.isi;

import edziekanat.isi.dto.GithubToken;
import edziekanat.isi.dto.GithubUserData;
import edziekanat.isi.dto.LoginRequest;
import edziekanat.isi.dto.UserPublicData;
import edziekanat.isi.exceptions.UserNotFoundException;
import edziekanat.isi.misc.CustomUserDetails;
import edziekanat.isi.models.OAuthProvider;
import edziekanat.isi.models.OAuthUser;
import edziekanat.isi.models.User;
import edziekanat.isi.models.UserRole;
import edziekanat.isi.repositories.OAuthProviderRepository;
import edziekanat.isi.repositories.OAuthUserRepository;
import edziekanat.isi.repositories.UserRepository;
import edziekanat.isi.repositories.UserRoleRepository;
import edziekanat.isi.services.AuthorizationService;
import edziekanat.isi.services.GithubApiClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@Transactional
class AuthorizationServiceIntegrationTest {

    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private OAuthUserRepository oAuthUserRepository;
    @Autowired
    private OAuthProviderRepository oAuthProviderRepository;
    @MockitoBean
    private GithubApiClient githubApiClient;

    @MockitoBean
    private HttpServletRequest request;
    @MockitoBean
    private HttpServletResponse response;

    @Test
    void loginShouldAuthenticateExistingUser() {

        UserRole role = userRoleRepository.findByName("student")
                .orElseThrow();

        User user = new User(
                role,
                "john",
                "test", // BCrypt hash
                "john@test.com",
                BCrypt.hashpw("password", BCrypt.gensalt())
        );

        userRepository.save(user);

        LoginRequest request = new LoginRequest("john@test.com", "password");
        UserPublicData result = authorizationService.login(request);

        assertNotNull(result);
        assertEquals("john@test.com", result.getEmail());
    }

    @Test
    void getUserPublicDataShouldReturnData() {
        UserRole role = userRoleRepository.findByName("student").orElseThrow();
        User user = userRepository.save(new User(role, "john", "password","john@test.com", ""));

        CustomUserDetails details = new CustomUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
        UserPublicData result = authorizationService.getUserPublicData(auth);

        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getUserPublicDataShouldThrowWhenUserMissing() {
        Optional<UserRole> role = userRoleRepository.findByName("student");

        User fakeUser = new User();
        fakeUser.setRole(role.get());
        fakeUser.setId(999L);

        CustomUserDetails details = new CustomUserDetails(fakeUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());

        assertThrows(UserNotFoundException.class, () -> authorizationService.getUserPublicData(auth));
    }

    @Test
    void shouldLoginExistingGithubUser() {

        GithubToken token = new GithubToken();
        token.setAccess_token("abc");

        GithubUserData githubUser = new GithubUserData();
        githubUser.setId(123L);

        Optional<OAuthProvider> oAuthProvider = oAuthProviderRepository.findById(0);

        UserRole role = userRoleRepository.findByName("student").orElseThrow();

        User user = userRepository.save(
                new User(
                        role,
                        "john",
                        "",
                        "john@test.com",
                        ""
                )
        );

        OAuthUser oauthUser = new OAuthUser();
        oauthUser.setProviderUserId("123");
        oauthUser.setUser(user);
        oauthUser.setProvider(oAuthProvider.get());

        oAuthUserRepository.save(oauthUser);

        when(githubApiClient.getToken(any())).thenReturn(ResponseEntity.ok(token));
        when(githubApiClient.getUserData(any())).thenReturn(ResponseEntity.ok(githubUser));

        authorizationService.githubCallback("code", request, response);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldRegisterAndLoginNewGithubUser() {
        GithubToken token = new GithubToken();
        token.setAccess_token("abc");

        GithubUserData githubUser = new GithubUserData();
        githubUser.setId(999L); // musi nie istniec w tabeli
        githubUser.setLogin("newGithubUser");
        githubUser.setEmail("new@test.com");

        OAuthProvider provider = oAuthProviderRepository.findByName("github").orElseThrow();

        UserRole role = userRoleRepository.findByName("student").orElseThrow();

        assertTrue(oAuthUserRepository.findByProviderUserId("999").isEmpty());

        when(githubApiClient.getToken(any())).thenReturn(ResponseEntity.ok(token));
        when(githubApiClient.getUserData(any())).thenReturn(ResponseEntity.ok(githubUser));


        authorizationService.githubCallback(
                "code",
                request,
                response
        );


        Optional<User> createdUser = userRepository.findByEmail("new@test.com");

        assertTrue(createdUser.isPresent());
        assertEquals("newGithubUser", createdUser.get().getName());
        assertEquals(role.getId(), createdUser.get().getRole().getId());

        Optional<OAuthUser> createdOAuth = oAuthUserRepository.findByProviderUserId("999");

        assertTrue(createdOAuth.isPresent());
        assertEquals(provider.getId(), createdOAuth.get().getProvider().getId());

        assertEquals("abc", createdOAuth.get().getToken());
        assertEquals(createdUser.get().getId(), createdOAuth.get().getUser().getId());

        // Powinien byc zalogowany
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);

        CustomUserDetails principal =
                (CustomUserDetails) authentication.getPrincipal();

        assertEquals(createdUser.get().getId(), principal.getUserId());
    }
}