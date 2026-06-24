package edziekanat.isi;

import edziekanat.isi.exceptions.UserNotFoundException;
import edziekanat.isi.misc.CustomUserDetails;
import edziekanat.isi.models.User;
import edziekanat.isi.models.UserRole;
import edziekanat.isi.repositories.UserRepository;
import edziekanat.isi.services.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private User user;

    @Test
    void testDetailsOfExistingUser() {
        String email = "test@mail.com";

        UserRole role = new UserRole();
        role.setName("STUDENT");

        User user = new User();
        user.setRole(role);
        user.setEmail(email);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        CustomUserDetails result =
                customUserDetailsService.loadUserByUsername(email);

        assertNotNull(result);
        assertEquals("ROLE_STUDENT", result.getAuthorities().iterator().next().getAuthority());

        verify(userRepository).findByEmail(email);
    }

    @Test
    void testUserNotFound() {
        String email = "missing@mail.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername(email)
        );

        verify(userRepository).findByEmail(email);
    }
}