package edziekanat.isi.services;

import edziekanat.isi.dto.CreateMessageRequest;
import edziekanat.isi.dto.CreatePropositionRequest;
import edziekanat.isi.dto.PropositionDTO;
import edziekanat.isi.dto.PropositionMessageDTO;
import edziekanat.isi.exceptions.FileErrorException;
import edziekanat.isi.exceptions.PropositionNotFoundException;
import edziekanat.isi.exceptions.UserNotFoundException;
import edziekanat.isi.models.Proposition;
import edziekanat.isi.models.PropositionFile;
import edziekanat.isi.models.PropositionMessage;
import edziekanat.isi.models.PropositionMessageFile;
import edziekanat.isi.repositories.PropositionMessageRepository;
import edziekanat.isi.repositories.PropositionRepository;
import edziekanat.isi.repositories.UserRepository;
import jakarta.el.PropertyNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PropositionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PropositionRepository propositionRepository;
    @Autowired
    private PropositionMessageRepository propositionMessageRepository;

    public List<PropositionDTO> getAllStudent(Long userId) {
        return propositionRepository.findAllByUserId(userId).stream().map(PropositionDTO::new).toList();
    }

    public List<PropositionMessageDTO> getAllPropositionMessages(Long propositionId) {
        return propositionMessageRepository.findAllByPropositionId(propositionId).stream().map(PropositionMessageDTO::new).toList();
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

        return new PropositionDTO(propositionRepository.save(proposition));
    }

    @Transactional
    public PropositionMessageDTO create(CreateMessageRequest dto, List<MultipartFile> files) {
        PropositionMessage message = new PropositionMessage();

        message.setProposition(propositionRepository.findById(dto.getPropositionId()).orElseThrow(PropositionNotFoundException::new));
        message.setUser(userRepository.findById(dto.getUserId()).orElseThrow(UserNotFoundException::new));
        message.setMessage(dto.getMessage());

        for(MultipartFile file : files){
            PropositionMessageFile mf = new PropositionMessageFile();

            mf.setFileName(file.getOriginalFilename());

            try {
                mf.setData(file.getBytes());
            }
            catch (IOException e) {
                throw new FileErrorException();
            }

            mf.setPropositionMessage(message);
            message.getFiles().add(mf);
        }

        return new PropositionMessageDTO(propositionMessageRepository.save(message));
    }
}