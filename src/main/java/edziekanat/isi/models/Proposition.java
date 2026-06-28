package edziekanat.isi.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="proposition")
public class Proposition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "proposition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropositionFile> files = new ArrayList<>();

    public Proposition() {
    }

    public Proposition(Long id, User user, String title, LocalDateTime createdAt, String description, List<PropositionFile> files) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.createdAt = createdAt;
        this.description = description;
        this.files = files;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<PropositionFile> getFiles() {
        return files;
    }

    public void setFiles(List<PropositionFile> files) {
        this.files = files;
    }
}