package edziekanat.isi;

import edziekanat.isi.dto.CreateMessageRequest;
import edziekanat.isi.dto.CreatePropositionRequest;
import edziekanat.isi.exceptions.FileErrorException;
import edziekanat.isi.exceptions.PropositionNotFoundException;
import edziekanat.isi.exceptions.UserNotFoundException;
import edziekanat.isi.models.Proposition;
import edziekanat.isi.models.PropositionMessage;
import edziekanat.isi.models.User;
import edziekanat.isi.repositories.*;
import edziekanat.isi.services.PropositionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropositionServiceTest {

    @InjectMocks
    private PropositionService propositionService;

    @Mock private UserRepository userRepository;
    @Mock private PropositionRepository propositionRepository;
    @Mock private PropositionFileRepository propositionFileRepository;
    @Mock private PropositionMessageRepository propositionMessageRepository;
    @Mock private PropositionMessageFileRepository propositionMessageFileRepository;

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        CreatePropositionRequest request = new CreatePropositionRequest();

        request.setUserId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> propositionService.create(request, null));
    }

    @Test
    void shouldThrowWhenPropositionDoesNotExist() {
        CreateMessageRequest request = new CreateMessageRequest();

        request.setPropositionId(1L);

        when(propositionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(PropositionNotFoundException.class,
                () -> propositionService.create(request, null));
    }

    @Test
    void shouldRejectSingleFileLargerThan10MB() {

        User user = new User();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        CreatePropositionRequest request = new CreatePropositionRequest();

        request.setUserId(1L);

        byte[] data = new byte[11 * 1024 * 1024];

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "large.pdf",
                        "application/pdf",
                        data);

        assertThrows(IllegalArgumentException.class,
                () -> propositionService.create(request, List.of(file)));
    }

    @Test
    void shouldRejectWhenTotalFileSizeExceeds50MB() {

        User user = new User();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        CreatePropositionRequest request = new CreatePropositionRequest();

        request.setUserId(1L);

        MockMultipartFile f1 =
                new MockMultipartFile("a", "a.pdf", "", new byte[26 * 1024 * 1024]);

        MockMultipartFile f2 =
                new MockMultipartFile("b", "b.pdf", "", new byte[26 * 1024 * 1024]);

        assertThrows(IllegalArgumentException.class,
                () -> propositionService.create(request, List.of(f1, f2)));
    }

    @Test
    void shouldThrowFileErrorWhenMultipartFails() throws IOException {

        User user = new User();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        CreatePropositionRequest request = new CreatePropositionRequest();

        request.setUserId(1L);

        org.springframework.web.multipart.MultipartFile multipart =
                mock(org.springframework.web.multipart.MultipartFile.class);

        when(multipart.isEmpty()).thenReturn(false);
        when(multipart.getSize()).thenReturn(100L);
        when(multipart.getOriginalFilename()).thenReturn("test.pdf");
        when(multipart.getBytes()).thenThrow(IOException.class);

        assertThrows(FileErrorException.class,
                () -> propositionService.create(request, List.of(multipart)));
    }

    @Test
    void shouldThrowWhenMessageFileDoesNotExist() {

        when(propositionMessageFileRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(FileErrorException.class,
                () -> propositionService.getMessageFile(1L));
    }

    @Test
    void shouldThrowWhenPropositionFileDoesNotExist() {

        when(propositionFileRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(FileErrorException.class,
                () -> propositionService.getPropositionFile(1L));
    }

}