package edziekanat.isi;

import edziekanat.isi.dto.CreateMessageRequest;
import edziekanat.isi.dto.CreatePropositionRequest;
import edziekanat.isi.dto.PropositionDTO;
import edziekanat.isi.dto.PropositionMessageDTO;
import edziekanat.isi.models.Proposition;
import edziekanat.isi.models.User;
import edziekanat.isi.models.UserRole;
import edziekanat.isi.repositories.PropositionRepository;
import edziekanat.isi.repositories.UserRepository;
import edziekanat.isi.repositories.UserRoleRepository;
import edziekanat.isi.services.PropositionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PropositionServiceIntegrationTest {

    @Autowired
    private PropositionService propositionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PropositionRepository propositionRepository;

    @Test
    void shouldCreatePropositionWithoutFiles() {

        UserRole role =
                userRoleRepository.findByName("student")
                        .orElseThrow();

        User user =
                userRepository.save(
                        new User(
                                role,
                                "john",
                                "",
                                "john@test.com",
                                ""
                        )
                );

        CreatePropositionRequest request =
                new CreatePropositionRequest();

        request.setUserId(user.getId());
        request.setTitle("Title");
        request.setDescription("Description");

        PropositionDTO dto =
                propositionService.create(request, null);

        assertNotNull(dto);
        assertEquals("Title", dto.getTitle());
    }

    @Test
    void shouldCreatePropositionWithFiles() {

        UserRole role =
                userRoleRepository.findByName("student")
                        .orElseThrow();

        User user =
                userRepository.save(
                        new User(
                                role,
                                "john",
                                "",
                                "john@test.com",
                                ""
                        )
                );

        CreatePropositionRequest request =
                new CreatePropositionRequest();

        request.setUserId(user.getId());
        request.setTitle("Test");

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "test.txt",
                        "text/plain",
                        "hello".getBytes()
                );

        PropositionDTO dto =
                propositionService.create(
                        request,
                        List.of(file)
                );

        Proposition proposition =
                propositionRepository.findById(dto.getId())
                        .orElseThrow();

        assertEquals(1, proposition.getFiles().size());
    }

    @Test
    void shouldCreateMessage() {

        UserRole role =
                userRoleRepository.findByName("student")
                        .orElseThrow();

        User user =
                userRepository.save(
                        new User(
                                role,
                                "john",
                                "",
                                "john@test.com",
                                ""
                        )
                );

        Proposition proposition =
                new Proposition();

        proposition.setUser(user);
        proposition.setTitle("Title");

        proposition =
                propositionRepository.save(proposition);

        CreateMessageRequest request =
                new CreateMessageRequest();

        request.setUserId(user.getId());
        request.setPropositionId(proposition.getId());
        request.setMessage("Hello");

        PropositionMessageDTO dto =
                propositionService.create(request, null);

        assertEquals("Hello", dto.getMessage());
    }

    @Test
    void shouldReturnStudentPropositions() {

        UserRole role =
                userRoleRepository.findByName("student")
                        .orElseThrow();

        User user =
                userRepository.save(
                        new User(
                                role,
                                "john",
                                "",
                                "john@test.com",
                                ""
                        )
                );

        Proposition proposition =
                new Proposition();

        proposition.setUser(user);
        proposition.setTitle("Example");

        propositionRepository.save(proposition);

        assertEquals(
                1,
                propositionService
                        .getAllStudent(
                                user.getId(),
                                org.springframework.data.domain.PageRequest.of(0,10)
                        )
                        .getTotalElements()
        );
    }
}