package edziekanat.isi.dto;

import edziekanat.isi.models.Proposition;
import edziekanat.isi.models.PropositionFile;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PropositionDTO {
    private Long id;
    private UserPublicData user;
    private LocalDateTime createdAt;
    private String title;
    private String description;
    private List<PropositionFileDTO> files = new ArrayList<>();

    public PropositionDTO() {
    }

    public PropositionDTO(Long id, UserPublicData user, LocalDateTime createdAt, String title, String description, List<PropositionFileDTO> files) {
        this.id = id;
        this.user = user;
        this.createdAt = createdAt;
        this.title = title;
        this.description = description;
        this.files = files;
    }

    public PropositionDTO(Proposition proposition) {
        this.id = proposition.getId();
        this.user = new UserPublicData(proposition.getUser());
        this.createdAt = proposition.getCreatedAt();
        this.title = proposition.getTitle();
        this.description = proposition.getDescription();
        this.files = proposition.getFiles().stream().map(PropositionFileDTO::new).toList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PropositionFileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<PropositionFileDTO> files) {
        this.files = files;
    }
}
