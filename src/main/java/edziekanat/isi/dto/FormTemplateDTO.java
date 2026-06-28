package edziekanat.isi.dto;

import edziekanat.isi.misc.FormFilledField;
import edziekanat.isi.models.FormTemplate;
import edziekanat.isi.misc.FormField;

import java.util.ArrayList;
import java.util.List;

//consider changing the name
public class FormTemplateDTO {
    private Integer id;
    private String title;
    private String response;
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

    public FormTemplateDTO(Integer id, String title, String response, List<FormField> formFields, List<FormFilledField> formFilledFields, Integer statusId) {
        this.id = id;
        this.title = title;
        this.response = response;
        this.formFields = formFields;
        this.formFilledFields = formFilledFields;
        this.statusId = statusId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<FormField> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<FormField> formFields) {
        this.formFields = formFields;
    }

    public List<FormFilledField> getFormFilledFields() {
        return formFilledFields;
    }

    public void setFormFilledFields(List<FormFilledField> formFilledFields) {
        this.formFilledFields = formFilledFields;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
}