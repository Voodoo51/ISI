package edziekanat.isi.dto;

import edziekanat.isi.models.Proposition;
import edziekanat.isi.models.PropositionMessage;
import edziekanat.isi.models.PropositionMessageFile;
import edziekanat.isi.models.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PropositionMessageDTO {
    private Long id;
    private PropositionDTO proposition;
    private UserPublicData user;
    private LocalDateTime createdAt;
    private String message;
    private List<PropositionMessageFileDTO> files = new ArrayList<>();

    public PropositionMessageDTO() {
    }

    public PropositionMessageDTO(Long id, Proposition proposition, UserPublicData user, LocalDateTime createdAt, String message, List<PropositionMessageFileDTO> files) {
        this.id = id;
        this.proposition = new PropositionDTO(proposition);
        this.user = user;
        this.createdAt = createdAt;
        this.message = message;
        this.files = files;
    }

    public PropositionMessageDTO(PropositionMessage propositionMessage) {
        this.id = propositionMessage.getId();
        this.proposition = new PropositionDTO(propositionMessage.getProposition());
        this.user = new UserPublicData(propositionMessage.getUser());
        this.createdAt = propositionMessage.getCreatedAt();
        this.message = propositionMessage.getMessage();
        this.files = propositionMessage.getFiles().stream().map(PropositionMessageFileDTO::new).toList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PropositionDTO getProposition() {
        return proposition;
    }

    public void setProposition(PropositionDTO proposition) {
        this.proposition = proposition;
    }

    public UserPublicData getUser() {
        return user;
    }

    public void setUser(UserPublicData user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PropositionMessageFileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<PropositionMessageFileDTO> files) {
        this.files = files;
    }
}
