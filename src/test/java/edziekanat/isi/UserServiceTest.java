package edziekanat.isi;

import edziekanat.isi.dto.RegisterRequest;
import edziekanat.isi.dto.UserPublicData;
import edziekanat.isi.misc.CustomUserDetails;
import edziekanat.isi.models.User;
import edziekanat.isi.models.UserRole;
import edziekanat.isi.repositories.UserRepository;
import edziekanat.isi.repositories.UserRoleRepository;
import edziekanat.isi.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Authentication authentication;

    private void mockAuth() {
        CustomUserDetails details = mock(CustomUserDetails.class);

        when(authentication.getPrincipal())
                .thenReturn(details);
    }

    @Test
    void testRegisterUser() {
        RegisterRequest request = mock(RegisterRequest.class);

        UserRole role = new UserRole();
        role.setId(1);
        role.setName("student");

        when(request.getRole()).thenReturn(role);
        when(request.getName()).thenReturn("Jan");
        when(request.getSurname()).thenReturn("Kowalski");
        when(request.getEmail()).thenReturn("jan@mail.com");
        when(request.getPassword()).thenReturn("plainPassword");

        when(userRoleRepository.findById(1))
                .thenReturn(Optional.of(role));

        when(passwordEncoder.encode("plainPassword"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        mockAuth();   // <-- add this back


        UserPublicData result =
                userService.register(request, authentication);


        assertNotNull(result);

        verify(passwordEncoder)
                .encode("plainPassword");

        verify(userRepository)
                .save(any(User.class));
    }

    @Test
    void testSaveUserWithEncodedPassword() {
        RegisterRequest request = mock(RegisterRequest.class);

        UserRole role = new UserRole();
        role.setId(1);
        role.setName("student");

        when(request.getRole()).thenReturn(role);
        when(request.getName()).thenReturn("Jan");
        when(request.getSurname()).thenReturn("Kowalski");
        when(request.getEmail()).thenReturn("jan@mail.com");
        when(request.getPassword()).thenReturn("plainPassword");


        when(userRoleRepository.findById(1))
                .thenReturn(Optional.of(role));

        when(passwordEncoder.encode("plainPassword"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        mockAuth();   // <-- add


        userService.register(request, authentication);


        verify(passwordEncoder)
                .encode("plainPassword");
    }

    @Test
    void testEncodeBeforeSaving() {
        RegisterRequest request = mock(RegisterRequest.class);

        UserRole role = new UserRole();
        role.setId(1);
        role.setName("student");

        when(request.getRole()).thenReturn(role);
        when(request.getName()).thenReturn("Jan");
        when(request.getSurname()).thenReturn("Kowalski");
        when(request.getEmail()).thenReturn("jan@mail.com");
        when(request.getPassword()).thenReturn("raw");


        when(userRoleRepository.findById(1))
                .thenReturn(Optional.of(role));

        when(passwordEncoder.encode("raw"))
                .thenReturn("hashed");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        mockAuth();   // <-- add


        userService.register(request, authentication);


        verify(passwordEncoder)
                .encode("raw");
    }
}