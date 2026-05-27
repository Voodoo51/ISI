package edziekanat.isi.dto;

import edziekanat.isi.misc.FormFilledField;

import java.util.List;

public class SendFormRequest {
    private Integer formTemplateId;
    private List<FormFilledField> formData;

    public Integer getFormTemplateId() {
        return formTemplateId;
    }

    public void setFormTemplateId(Integer formTemplateId) {
        this.formTemplateId = formTemplateId;
    }

    public List<FormFilledField> getFormData() {
        return formData;
    }

    public void setFormData(List<FormFilledField> formData) {
        this.formData = formData;
    }
}