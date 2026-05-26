package edziekanat.isi.models;

import jakarta.persistence.*;

@Entity
@Table(name="sent_form")
public class SentForm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name="form_template_id", nullable = false)
    private FormTemplate formTemplate;
    @ManyToOne
    @JoinColumn(name="status_id", nullable = false)
    private SentFormStatus status;
    private String json;

    public SentForm() {
    }

    public SentForm(long id, User user, FormTemplate formTemplate, SentFormStatus status, String json) {
        this.id = id;
        this.user = user;
        this.formTemplate = formTemplate;
        this.status = status;
        this.json = json;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FormTemplate getFormTemplate() {
        return formTemplate;
    }

    public void setFormTemplate(FormTemplate formTemplate) {
        this.formTemplate = formTemplate;
    }

    public SentFormStatus getStatus() {
        return status;
    }

    public void setStatus(SentFormStatus status) {
        this.status = status;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
