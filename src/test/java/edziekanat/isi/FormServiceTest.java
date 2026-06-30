package edziekanat.isi;

import edziekanat.isi.dto.*;
import edziekanat.isi.exceptions.FormTemplateNotFoundException;
import edziekanat.isi.exceptions.UnauthorizedException;
import edziekanat.isi.misc.CustomUserDetails;
import edziekanat.isi.misc.SentFormStatusE;
import edziekanat.isi.models.*;
import edziekanat.isi.repositories.FormTemplateRepository;
import edziekanat.isi.repositories.SentFormRepository;
import edziekanat.isi.repositories.SentFormStatusRepository;
import edziekanat.isi.repositories.UserRepository;
import edziekanat.isi.services.FormService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FormServiceTest {
    @InjectMocks
    private FormService formService;
    @Mock
    private SentFormRepository sentFormRepository;
    @Mock private SentFormStatusRepository sentFormStatusRepository;
    @Mock private FormTemplateRepository formTemplateRepository;
    @Mock private UserRepository userRepository;
    @Mock private Authentication authentication;

    @Test
    void shouldReturnEmptyListWhenUserIdIsNull() {
        List<SentFormDTO> result = formService.getSentForms(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetSentForms() {
        FormTemplate template = new FormTemplate();
        template.setId(1);

        User user = new User();
        user.setId(1L);

        UserRole role = new UserRole();
        role.setName("student");

        user.setRole(role);

        SentForm sentForm = new SentForm();
        sentForm.setId(1L);
        sentForm.setUser(user);
        sentForm.setStatus(new SentFormStatus(1, "status"));
        sentForm.setFormTemplate(template);

        when(sentFormRepository.findByUserId(1L))
                .thenReturn(List.of(sentForm));

        List<SentFormDTO> result = formService.getSentForms(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testCreateFormTemplate() throws IOException {
        FormTemplateCreationRequest request = mock(FormTemplateCreationRequest.class);

        MultipartFile file = mock(MultipartFile.class);

        when(request.getTitle()).thenReturn("Test");
        when(request.getFormFields()).thenReturn(new ArrayList<>());

        when(file.getBytes())
                .thenReturn(new byte[]{1,2,3});

        when(file.getOriginalFilename())
                .thenReturn("test.pdf");


        formService.createFormTemplate(request, file);

        verify(formTemplateRepository).save(any(FormTemplate.class));
    }

    private CustomUserDetails mockAuth(Long userId) {
        CustomUserDetails details = mock(CustomUserDetails.class);

        when(authentication.getPrincipal()).thenReturn(details);
        when(details.getUserId()).thenReturn(userId);

        return details;
    }

    @Test
    void testReturnFormTemplates() {
        mockAuth(1L);

        FormTemplate template = mock(FormTemplate.class);
        when(template.getId()).thenReturn(10);

        when(formTemplateRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(template)));

        when(sentFormRepository.findByUserId(1L))
                .thenReturn(new ArrayList<>());

        Page<StudentSentFormDTO> result =
                formService.getFormTemplates(
                        null,
                        PageRequest.of(0, 10),
                        authentication
                );

        assertEquals(1, result.getContent().size());
        assertEquals(3, result.getContent().get(0).getStatusId());
    }

    @Test
    void testReturnFormTemplate() {
        mockAuth(1L);

        FormTemplate template = mock(FormTemplate.class);
        when(template.getId()).thenReturn(5);

        when(formTemplateRepository.findById(5))
                .thenReturn(Optional.of(template));

        when(sentFormRepository.findByUserIdAndFormTemplateId(1L, 5))
                .thenReturn(Optional.empty());

        FormTemplateDTO result =  formService.getTemplate(5, 1L);

        assertNotNull(result);
        assertEquals(3, result.getStatusId());
    }

    @Test
    void testTemplateNotFound() {
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUserId()).thenReturn(1L);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(formTemplateRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(FormTemplateNotFoundException.class, () ->
                formService.getTemplate(99, 1L)
        );
    }

    @Test
    void testCreateNewForm() {
        mockAuth(1L);

        SendFormRequest request = mock(SendFormRequest.class);

        FormTemplate template = mock(FormTemplate.class);
        User user = mock(User.class);
        SentFormStatus status = mock(SentFormStatus.class);

        when(request.getFormTemplateId()).thenReturn(10);
        when(request.getFormData()).thenReturn(new ArrayList<>());

        when(user.getId()).thenReturn(1L);

        when(sentFormStatusRepository.findById(1))
                .thenReturn(Optional.of(status));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(formTemplateRepository.findById(10))
                .thenReturn(Optional.of(template));

        when(sentFormRepository.findByUserIdAndFormTemplateId(1L, 10))
                .thenReturn(Optional.empty());

        formService.sendForm(request, authentication);

        verify(sentFormRepository).save(any(SentForm.class));
    }

    @Test
    void testUpdateForm() {
        mockAuth(1L);

        SendFormRequest request = mock(SendFormRequest.class);

        FormTemplate template = mock(FormTemplate.class);
        User user = mock(User.class);
        SentFormStatus status = mock(SentFormStatus.class);
        SentForm formToUpdate = mock(SentForm.class);

        when(request.getFormTemplateId()).thenReturn(10);
        when(request.getFormData()).thenReturn(new ArrayList<>());

        when(status.getId())
                .thenReturn(SentFormStatusE.IN_NEED_OF_UPDATE.getId());

        when(formToUpdate.getStatus())
                .thenReturn(status);

        when(user.getId())
                .thenReturn(1L);

        when(sentFormStatusRepository.findById(1))
                .thenReturn(Optional.of(status));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(formTemplateRepository.findById(10))
                .thenReturn(Optional.of(template));

        when(sentFormRepository.findByUserIdAndFormTemplateId(1L, 10))
                .thenReturn(Optional.of(formToUpdate));

        formService.sendForm(request, authentication);

        verify(sentFormRepository).save(any(SentForm.class));
    }

    @Test
    void testNotAuthenticated() {
        assertThrows(UnauthorizedException.class, () ->
                formService.getFormTemplates(
                        null,
                        PageRequest.of(0, 10),
                        null
                )
        );
    }
}