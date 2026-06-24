package edziekanat.isi;

import edziekanat.isi.dto.LoginRequest;
import edziekanat.isi.dto.UserPublicData;
import edziekanat.isi.exceptions.BadRequestException;
import edziekanat.isi.exceptions.InternalServerErrorException;
import edziekanat.isi.exceptions.UserNotFoundException;
import edziekanat.isi.misc.CustomUserDetails;
import edziekanat.isi.models.User;
import edziekanat.isi.repositories.OAuthProviderRepository;
import edziekanat.isi.repositories.UserRepository;
import edziekanat.isi.services.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {
    @InjectMocks
    private AuthorizationService authorizationService;
    @Mock
    private UserRepository userRepository;
    @Mock private OAuthProviderRepository oAuthProviderRepository;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private User user;

    @Test
    void testGithubUrl() {
        String url = authorizationService.redirectGithub();

        assertTrue(url.contains("https://github.com/login/oauth/authorize"));
        assertTrue(url.contains("client_id="));
        assertTrue(url.contains("redirect_uri="));
        assertTrue(url.contains("scope="));
    }

    @Test
    void testMissingGithub() {
        when(oAuthProviderRepository.findByName("github"))
                .thenReturn(Optional.empty());

        assertThrows(InternalServerErrorException.class, () ->
                authorizationService.githubCallback("code", request, response)
        );
    }

    @Test
    void testSuccessfulLogin() {
        LoginRequest loginRequest = new LoginRequest("test@mail.com", "password");

        Authentication auth = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@mail.com");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        UserPublicData result = authorizationService.login(loginRequest);

        assertNotNull(result);
        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByEmail("test@mail.com");
    }

    @Test
    void testUnsuccessfulLogin() {
        LoginRequest loginRequest = new LoginRequest("test@mail.com", "password");

        Authentication auth = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@mail.com");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                authorizationService.login(loginRequest)
        );
    }

    @Test
    void testReturnUserData() {
        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(customUserDetails.getUserId()).thenReturn(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserPublicData result =
                authorizationService.getUserPublicData(authentication);

        assertNotNull(result);
        verify(userRepository).findById(1L);
    }

    @Test
    void testNullUser() {
        assertThrows(BadRequestException.class, () ->
                authorizationService.getUserPublicData(null)
        );
    }
}