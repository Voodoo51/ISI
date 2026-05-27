package edziekanat.isi.dto;

import edziekanat.isi.misc.FormField;

import java.util.List;

public class FormTemplateCreationRequest {
    private String title;
    private List<FormField> formFields;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FormField>  getFormFields() {
        return formFields;
    }

    public void setFormFields(List<FormField>  formFields) {
        this.formFields = formFields;
    }
}
