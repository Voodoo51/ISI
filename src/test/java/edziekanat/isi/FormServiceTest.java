package edziekanat.isi;

import edziekanat.isi.dto.*;
import edziekanat.isi.exceptions.*;
import edziekanat.isi.misc.CustomUserDetails;
import edziekanat.isi.misc.FormField;
import edziekanat.isi.misc.FormFilledField;
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
    void createFormTemplateShouldThrowWhenFileCannotBeRead() throws IOException {

        FormTemplateCreationRequest request = mock(FormTemplateCreationRequest.class);
        MultipartFile file = mock(MultipartFile.class);

        when(request.getTitle()).thenReturn("Test");
        when(request.getFormFields()).thenReturn(new ArrayList<>());

        when(file.getBytes()).thenThrow(IOException.class);

        assertThrows(
                FileErrorException.class,
                () -> formService.createFormTemplate(request, file)
        );
    }

    @Test
    void shouldReturnTemplateWithExistingSubmission() {

        FormTemplate template = new FormTemplate();
        template.setId(5);

        SentFormStatus status = new SentFormStatus(2, "accepted");

        SentForm sentForm = new SentForm();
        sentForm.setStatus(status);
        sentForm.setResponse("Approved");

        when(formTemplateRepository.findById(5))
                .thenReturn(Optional.of(template));

        when(sentFormRepository.findByUserIdAndFormTemplateId(1L, 5))
                .thenReturn(Optional.of(sentForm));

        FormTemplateDTO dto = formService.getTemplate(5, 1L);

        assertEquals(2, dto.getStatusId());
        assertEquals("Approved", dto.getResponse());
    }

    @Test
    void sendFormShouldThrowWhenStatusMissing() {

        mockAuth(1L);

        SendFormRequest request = mock(SendFormRequest.class);

        when(sentFormStatusRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                InternalServerErrorException.class,
                () -> formService.sendForm(request, authentication)
        );
    }

    @Test
    void sendFormShouldThrowWhenUserMissing() {

        mockAuth(1L);

        SendFormRequest request = mock(SendFormRequest.class);

        SentFormStatus status = mock(SentFormStatus.class);

        when(sentFormStatusRepository.findById(1))
                .thenReturn(Optional.of(status));

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> formService.sendForm(request, authentication)
        );
    }

    @Test
    void sendFormShouldThrowWhenTemplateMissing() {

        mockAuth(1L);

        SendFormRequest request = mock(SendFormRequest.class);

        when(request.getFormTemplateId()).thenReturn(7);

        User user = mock(User.class);
        SentFormStatus status = mock(SentFormStatus.class);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(sentFormStatusRepository.findById(1))
                .thenReturn(Optional.of(status));

        when(formTemplateRepository.findById(7))
                .thenReturn(Optional.empty());

        assertThrows(
                InternalServerErrorException.class,
                () -> formService.sendForm(request, authentication)
        );
    }

    @Test
    void shouldNotAllowSendingSameFormTwice() {

        mockAuth(1L);

        SendFormRequest request = mock(SendFormRequest.class);

        when(request.getFormTemplateId()).thenReturn(5);
        when(request.getFormData()).thenReturn(new ArrayList<>());

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        FormTemplate template = mock(FormTemplate.class);

        SentFormStatus pending = mock(SentFormStatus.class);
        when(pending.getId()).thenReturn(1);

        SentForm sent = mock(SentForm.class);
        when(sent.getStatus()).thenReturn(pending);

        when(sentFormStatusRepository.findById(1))
                .thenReturn(Optional.of(pending));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(formTemplateRepository.findById(5))
                .thenReturn(Optional.of(template));

        when(sentFormRepository.findByUserIdAndFormTemplateId(1L, 5))
                .thenReturn(Optional.of(sent));

        assertThrows(
                UnauthorizedException.class,
                () -> formService.sendForm(request, authentication)
        );
    }

    @Test
    void sendFormShouldRejectInvalidPhoneNumber() {
        mockAuth(1L);

        FormField field = new FormField();
        field.setId(1);
        field.setLabel("Phone");
        field.setType("phoneNumber");

        FormTemplate template = new FormTemplate();
        template.setFormFields(List.of(field));

        FormFilledField filled = new FormFilledField();
        filled.setId(1);
        filled.setValue("abc123");

        SendFormRequest request = mock(SendFormRequest.class);

        when(request.getFormTemplateId()).thenReturn(1);
        when(request.getFormData()).thenReturn(List.of(filled));

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        SentFormStatus status = new SentFormStatus(1, "pending");

        when(sentFormStatusRepository.findById(1)).thenReturn(Optional.of(status));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(formTemplateRepository.findById(1)).thenReturn(Optional.of(template));

        assertThrows(BadRequestException.class,
                () -> formService.sendForm(request, authentication));
    }

    @Test
    void sendFormShouldRejectInvalidPesel() {
        mockAuth(1L);

        FormField field = new FormField();
        field.setId(1);
        field.setLabel("PESEL");
        field.setType("pesel");

        FormTemplate template = new FormTemplate();
        template.setFormFields(List.of(field));

        FormFilledField filled = new FormFilledField();
        filled.setId(1);
        filled.setValue("12345");

        SendFormRequest request = mock(SendFormRequest.class);

        when(request.getFormTemplateId()).thenReturn(1);
        when(request.getFormData()).thenReturn(List.of(filled));

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        SentFormStatus status = new SentFormStatus(1, "pending");

        when(sentFormStatusRepository.findById(1)).thenReturn(Optional.of(status));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(formTemplateRepository.findById(1)).thenReturn(Optional.of(template));

        assertThrows(BadRequestException.class,
                () -> formService.sendForm(request, authentication));
    }

    @Test
    void sendFormShouldRejectWhenFieldIsMissing() {
        mockAuth(1L);

        FormField field1 = new FormField();
        field1.setId(1);
        field1.setLabel("First");
        field1.setType("text");

        FormField field2 = new FormField();
        field2.setId(2);
        field2.setLabel("Second");
        field2.setType("text");

        FormTemplate template = new FormTemplate();
        template.setFormFields(List.of(field1, field2));

        FormFilledField filled = new FormFilledField();
        filled.setId(1);
        filled.setValue("value");

        SendFormRequest request = mock(SendFormRequest.class);

        when(request.getFormTemplateId()).thenReturn(1);
        when(request.getFormData()).thenReturn(List.of(filled));

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        SentFormStatus status = new SentFormStatus(1, "pending");

        when(sentFormStatusRepository.findById(1)).thenReturn(Optional.of(status));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(formTemplateRepository.findById(1)).thenReturn(Optional.of(template));

        assertThrows(BadRequestException.class,
                () -> formService.sendForm(request, authentication));
    }

    @Test
    void shouldReturnEmptySentFormsList() {

        when(sentFormRepository.findByUserId(1L))
                .thenReturn(List.of());

        List<SentFormDTO> result = formService.getSentForms(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldUpdateSentFormStatus() {

        mockAuth(1L);

        UpdateSentFormRequest request = new UpdateSentFormRequest();

        request.setSentFormId(1L);
        request.setNewStatusId(2);
        request.setResponse("Accepted");

        SentForm form = new SentForm();
        SentFormStatus status = new SentFormStatus(2, "accepted");

        when(sentFormRepository.findById(1L))
                .thenReturn(Optional.of(form));

        when(sentFormStatusRepository.findById(2))
                .thenReturn(Optional.of(status));

        formService.updateSentFormStatus(request, authentication);

        verify(sentFormRepository).save(form);

        assertEquals("Accepted", form.getResponse());
        assertEquals(status, form.getStatus());
    }

    @Test
    void updateSentFormStatusShouldThrowWhenFormMissing() {

        mockAuth(1L);

        UpdateSentFormRequest request = new UpdateSentFormRequest();

        request.setSentFormId(100L);

        when(sentFormRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                SentFormNotFoundException.class,
                () -> formService.updateSentFormStatus(request, authentication)
        );
    }

    @Test
    void shouldReturnTemplatePdf() {

        FormTemplate template = new FormTemplate();
        template.setId(5);
        template.setForm_pdf(new byte[]{1,2,3});

        when(formTemplateRepository.findById(5))
                .thenReturn(Optional.of(template));

        FormTemplateFileDTO dto =
                formService.getTemplatePdf(5);

        assertArrayEquals(new byte[]{1,2,3}, dto.getPdfFile());
    }

    @Test
    void shouldThrowWhenTemplatePdfNotFound() {

        when(formTemplateRepository.findById(5))
                .thenReturn(Optional.empty());

        assertThrows(
                FormTemplateNotFoundException.class,
                () -> formService.getTemplatePdf(5)
        );
    }

    @Test
    void sendFormShouldRejectInvalidEmail() {
        mockAuth(1L);

        FormField field = new FormField();
        field.setId(1);
        field.setLabel("Email");
        field.setType("email");

        FormTemplate template = new FormTemplate();
        template.setFormFields(List.of(field));

        FormFilledField filled = new FormFilledField();
        filled.setId(1);
        filled.setValue("not-an-email");

        SendFormRequest request = mock(SendFormRequest.class);

        when(request.getFormTemplateId()).thenReturn(1);
        when(request.getFormData()).thenReturn(List.of(filled));

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        SentFormStatus status = new SentFormStatus(1, "pending");

        when(sentFormStatusRepository.findById(1)).thenReturn(Optional.of(status));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(formTemplateRepository.findById(1)).thenReturn(Optional.of(template));

        assertThrows(BadRequestException.class,
                () -> formService.sendForm(request, authentication));
    }

    @Test
    void searchSentFormsShouldUseAdminRepositoryMethod() {

        Authentication auth = mock(Authentication.class);

        when(auth.getName()).thenReturn("admin@test.com");

        UserRole role = new UserRole();
        role.setName("admin");

        User user = new User();
        user.setRole(role);

        when(userRepository.findByEmail("admin@test.com"))
                .thenReturn(Optional.of(user));

        when(sentFormRepository.search(any(), any(), any()))
                .thenReturn(Page.empty());

        formService.searchSentForms(
                "test",
                null,
                PageRequest.of(0, 10),
                auth
        );

        verify(sentFormRepository).search(
                eq("test"),
                isNull(),
                any(Pageable.class)
        );

        verify(sentFormRepository, never())
                .searchUserForms(anyLong(), any(), any(), any());
    }

    @Test
    void searchSentFormsShouldUseAdminSearch() {

        Authentication auth = mock(Authentication.class);

        when(auth.getName()).thenReturn("admin@test.com");

        UserRole role = new UserRole();
        role.setName("admin");

        User user = new User();
        user.setRole(role);

        when(userRepository.findByEmail("admin@test.com"))
                .thenReturn(Optional.of(user));

        when(sentFormRepository.search(any(), any(), any()))
                .thenReturn(Page.empty());

        formService.searchSentForms(
                "abc",
                null,
                PageRequest.of(0,10),
                auth
        );

        verify(sentFormRepository).search(any(), any(), any());
        verify(sentFormRepository, never())
                .searchUserForms(any(), any(), any(), any());
    }

    @Test
    void searchSentFormsShouldUseStudentSearch() {

        Authentication auth = mock(Authentication.class);

        when(auth.getName()).thenReturn("student@test.com");

        UserRole role = new UserRole();
        role.setName("student");

        User user = new User();
        user.setId(5L);
        user.setRole(role);

        when(userRepository.findByEmail("student@test.com"))
                .thenReturn(Optional.of(user));

        when(sentFormRepository.searchUserForms(any(), any(), any(), any()))
                .thenReturn(Page.empty());

        formService.searchSentForms(
                "abc",
                null,
                PageRequest.of(0,10),
                auth
        );

        verify(sentFormRepository).searchUserForms(
                eq(5L),
                any(),
                any(),
                any()
        );

        verify(sentFormRepository, never())
                .search(any(), any(), any());
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