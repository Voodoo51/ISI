package edziekanat.isi.dto;

import edziekanat.isi.misc.FormFilledField;
import edziekanat.isi.models.FormTemplate;
import edziekanat.isi.misc.FormField;

import java.util.ArrayList;
import java.util.List;

public class FormTemplateDTO {
    private Integer id;
    private String title;
    private List<FormField> formFields;
    private List<FormFilledField> formFilledFields;
    private Integer statusId;

    public FormTemplateDTO() {
    }

    public FormTemplateDTO(FormTemplate formTemplate) {
        this.id = formTemplate.getId();
        this.title = formTemplate.getTitle();
        this.formFields = formTemplate.getFormFields() != null ? formTemplate.getFormFields() : new ArrayList<>(); //mostly just so that hardcoded templates work
        this.statusId = 3;
    }

    public FormTemplateDTO(Integer id, String title, List<FormField> formFields, List<FormFilledField> formFilledFields) {
        this.id = id;
        this.title = title;
        this.formFields = formFields;
        this.formFilledFields = formFilledFields;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setFormFilledFields(List<FormFilledField> formFilledFields) {
        this.formFilledFields = formFilledFields;
    }

    public List<FormField> getFormFields() {
        return formFields;
    }

    public List<FormFilledField> getFormFilledFields() {
        return formFilledFields;
    }
}