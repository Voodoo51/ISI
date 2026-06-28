package edziekanat.isi.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="proposition_message")
public class PropositionMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="proposition_id")
    private Proposition proposition;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition="TEXT")
    private String message;

    @OneToMany(mappedBy="propositionMessage",
            cascade = CascadeType.ALL)
    private List<PropositionMessageFile> files = new ArrayList<>();

    public PropositionMessage() {
    }

    public PropositionMessage(Long id, Proposition proposition, User user, LocalDateTime createdAt, String message, List<PropositionMessageFile> files) {
        this.id = id;
        this.proposition = proposition;
        this.user = user;
        this.createdAt = createdAt;
        this.message = message;
        this.files = files;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proposition getProposition() {
        return proposition;
    }

    public void setProposition(Proposition proposition) {
        this.proposition = proposition;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public List<PropositionMessageFile> getFiles() {
        return files;
    }

    public void setFiles(List<PropositionMessageFile> files) {
        this.files = files;
    }
}