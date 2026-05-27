package edziekanat.isi.models;

import edziekanat.isi.misc.FormField;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.relational.core.sql.In;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="form_template")
public class FormTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    private String title;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "form_fields", columnDefinition = "jsonb")
    private List<FormField> formFields;
    public FormTemplate() {
    }

    public FormTemplate(Integer id, LocalDateTime createdAt, String title, List<FormField> formFields) {
        this.id = id;
        this.createdAt = createdAt;
        this.title = title;
        this.formFields = formFields;
    }

    public FormTemplate(LocalDateTime createdAt, String title, List<FormField> formFields) {
        this.createdAt = createdAt;
        this.title = title;
        this.formFields = formFields;
    }

    /*
    public FormTemplate(String title, String formFields, LocalDateTime createdAt) {
        this.title = title;
        this.formFields = formFields;
        this.createdAt = createdAt;
    }

    public FormTemplate(int id, String title, String formFields, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.formFields = formFields;
        this.createdAt = createdAt;
    }
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<FormField> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<FormField> formFields) {
        this.formFields = formFields;
    }
}
