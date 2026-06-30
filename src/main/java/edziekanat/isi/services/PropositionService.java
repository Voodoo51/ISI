package edziekanat.isi.services;

import edziekanat.isi.dto.*;
import edziekanat.isi.exceptions.FileErrorException;
import edziekanat.isi.exceptions.PropositionNotFoundException;
import edziekanat.isi.exceptions.UserNotFoundException;
import edziekanat.isi.models.Proposition;
import edziekanat.isi.models.PropositionFile;
import edziekanat.isi.models.PropositionMessage;
import edziekanat.isi.models.PropositionMessageFile;
import edziekanat.isi.repositories.*;
import jakarta.el.PropertyNotFoundException;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PropositionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PropositionRepository propositionRepository;
    @Autowired
    private PropositionFileRepository propositionFileRepository;
    @Autowired
    private PropositionMessageRepository propositionMessageRepository;
    @Autowired
    private PropositionMessageFileRepository propositionMessageFileRepository;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final long MAX_TOTAL_SIZE = 50 * 1024 * 1024;

    public Page<PropositionDTO> getAll(Pageable pageable) {
        return propositionRepository.findAll(pageable).map(PropositionDTO::new);
    }

    public Page<PropositionDTO> getAllStudent(Long userId, Pageable pageable) {
        return propositionRepository.findAllByUserId(userId, pageable).map(PropositionDTO::new);
    }

    public Page<PropositionDTO> search(String query, Pageable pageable) {
        return propositionRepository.search(query, pageable).map(PropositionDTO::new);
    }

    public Page<PropositionDTO> searchUser(Long userId, String query, Pageable pageable) {
        return propositionRepository.searchUser(userId, query, pageable).map(PropositionDTO::new);
    }

    public Page<PropositionMessageDTO> getAllPropositionMessages(
            Long propositionId,
            Pageable pageable
    ) {
        Page<PropositionMessage> page =
                propositionMessageRepository.findAllByPropositionIdOrderByCreatedAtDesc(
                        propositionId,
                        pageable
                );

        List<PropositionMessageDTO> content =
                page.getContent()
                        .stream()
                        .map(PropositionMessageDTO::new)
                        .sorted(Comparator.comparing(PropositionMessageDTO::getCreatedAt))
                        .toList();

        return new PageImpl<>(
                content,
                pageable,
                page.getTotalElements()
        );
    }

    public PropositionDTO getStudentPropositionInfo(Long propositionId) {
        Proposition proposition = propositionRepository.findById(propositionId).orElseThrow(PropertyNotFoundException::new);

        return new PropositionDTO(proposition);
    }

    @Transactional
    public PropositionDTO create(CreatePropositionRequest request, List<MultipartFile> files) {
        Proposition proposition = new Proposition();

        proposition.setUser(userRepository.findById(request.getUserId()).orElseThrow(UserNotFoundException::new));
        proposition.setTitle(request.getTitle());
        proposition.setDescription(request.getDescription());

        if(files != null) {
            validateFiles(files);

            for(MultipartFile file : files){
                PropositionFile pf = new PropositionFile();

                pf.setFileName(file.getOriginalFilename());
                try {
                    pf.setData(file.getBytes());
                }
                catch (IOException e) {
                    throw new FileErrorException();
                }
                pf.setProposition(proposition);

                proposition.getFiles().add(pf);
            }
        }


        return new PropositionDTO(propositionRepository.save(proposition));
    }

    @Transactional
    public PropositionMessageDTO create(CreateMessageRequest dto, List<MultipartFile> files) {
        PropositionMessage message = new PropositionMessage();

        message.setProposition(propositionRepository.findById(dto.getPropositionId()).orElseThrow(PropositionNotFoundException::new));
        message.setUser(userRepository.findById(dto.getUserId()).orElseThrow(UserNotFoundException::new));
        message.setMessage(dto.getMessage());


        if(files != null) {
            validateFiles(files);

            for (MultipartFile file : files) {
                PropositionMessageFile mf = new PropositionMessageFile();

                mf.setFileName(file.getOriginalFilename());

                try {
                    mf.setData(file.getBytes());
                } catch (IOException e) {
                    throw new FileErrorException();
                }

                mf.setPropositionMessage(message);
                message.getFiles().add(mf);
            }
        }

        return new PropositionMessageDTO(propositionMessageRepository.save(message));
    }



    private void validateFiles(List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return;
        }

        long totalSize = 0;

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue;
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException(
                        file.getOriginalFilename() + " exceeds the maximum file size of 10 MB.");
            }

            totalSize += file.getSize();
        }

        if (totalSize > MAX_TOTAL_SIZE) {
            throw new IllegalArgumentException(
                    "The total upload size exceeds the maximum allowed size of 50 MB.");
        }
    }

    public PropositionFileRequest getMessageFile(Long fileId) {
        Optional<PropositionMessageFile> propositionMessageFile = propositionMessageFileRepository.findById(fileId);

        if(propositionMessageFile.isEmpty()) throw new FileErrorException();

        return new PropositionFileRequest(propositionMessageFile.get());
    }

    public PropositionFileRequest getPropositionFile(Long fileId) {
        Optional<PropositionFile> propositionFile = propositionFileRepository.findById(fileId);

        if(propositionFile.isEmpty()) throw new FileErrorException();

        return new PropositionFileRequest(propositionFile.get());
    }
}