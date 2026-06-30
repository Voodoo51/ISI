package edziekanat.isi.dto;

import edziekanat.isi.misc.FormField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class FormTemplateCreationRequest {
    @NotBlank(message = "formCreationTitleEmptyError")
    private String title;
    @NotEmpty(message = "formCreationFieldsEmptyError")
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
