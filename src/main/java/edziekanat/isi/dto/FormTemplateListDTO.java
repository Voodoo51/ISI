package edziekanat.isi.dto;

public class FormTemplateListDTO {
    private Integer templateId;
    private Integer statusId;
    private String title;

    public FormTemplateListDTO(Integer templateId, Integer statusId, String title) {
        this.templateId = templateId;
        this.statusId = statusId;
        this.title = title;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getTitle() {
        return title;
    }
}