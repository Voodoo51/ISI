package edziekanat.isi.models;

import edziekanat.isi.misc.FormFilledField;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name="sent_form")
public class SentForm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name="form_template_id", nullable = false)
    private FormTemplate formTemplate;
    @ManyToOne
    @JoinColumn(name="status_id", nullable = false)
    private SentFormStatus status;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "form_data", columnDefinition = "jsonb")
    private List<FormFilledField> formData;

    public SentForm() {
    }

    public SentForm(Long id, User user, FormTemplate formTemplate, SentFormStatus status, List<FormFilledField> formData) {
        this.id = id;
        this.user = user;
        this.formTemplate = formTemplate;
        this.status = status;
        this.formData = formData;
    }

    public SentForm(User user, FormTemplate formTemplate, SentFormStatus status, List<FormFilledField> formData) {
        this.user = user;
        this.formTemplate = formTemplate;
        this.status = status;
        this.formData = formData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<FormFilledField> getFormData() {
        return formData;
    }

    public void setFormData(List<FormFilledField> formData) {
        this.formData = formData;
    }
}
