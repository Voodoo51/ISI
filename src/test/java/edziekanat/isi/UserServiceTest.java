package edziekanat.isi;

import edziekanat.isi.dto.RegisterRequest;
import edziekanat.isi.dto.UserPublicData;
import edziekanat.isi.models.User;
import edziekanat.isi.models.UserRole;
import edziekanat.isi.repositories.UserRepository;
import edziekanat.isi.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegisterUser() {
        RegisterRequest request = mock(RegisterRequest.class);

        UserRole role = new UserRole();
        role.setName("STUDENT");

        when(request.getRole()).thenReturn(role);
        when(request.getName()).thenReturn("Jan");
        when(request.getSurname()).thenReturn("Kowalski");
        when(request.getEmail()).thenReturn("jan@mail.com");
        when(request.getPassword()).thenReturn("plainPassword");

        when(passwordEncoder.encode("plainPassword"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserPublicData result = userService.register(request);

        assertNotNull(result);
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSaveUserWithEncodedPassword() {
        RegisterRequest request = mock(RegisterRequest.class);

        UserRole role = new UserRole();
        role.setName("STUDENT");

        when(request.getRole()).thenReturn(role);
        when(request.getName()).thenReturn("Jan");
        when(request.getSurname()).thenReturn("Kowalski");
        when(request.getEmail()).thenReturn("jan@mail.com");
        when(request.getPassword()).thenReturn("plainPassword");

        when(passwordEncoder.encode("plainPassword"))
                .thenReturn("encodedPassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        when(userRepository.save(userCaptor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        userService.register(request);

        User savedUser = userCaptor.getValue();

        assertEquals("Jan", savedUser.getName());
        assertEquals("Kowalski", savedUser.getSurname());
        assertEquals("jan@mail.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    @Test
    void testEncodeBeforeSaving() {
        RegisterRequest request = mock(RegisterRequest.class);

        when(request.getRole()).thenReturn(new UserRole());
        when(request.getName()).thenReturn("Jan");
        when(request.getSurname()).thenReturn("Kowalski");
        when(request.getEmail()).thenReturn("jan@mail.com");
        when(request.getPassword()).thenReturn("raw");

        when(passwordEncoder.encode("raw"))
                .thenReturn("hashed");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        userService.register(request);

        verify(passwordEncoder, times(1)).encode("raw");
    }
}