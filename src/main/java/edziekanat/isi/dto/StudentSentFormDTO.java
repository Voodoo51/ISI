package edziekanat.isi.dto;

//Student as in this is specifically made for student's view on the frontend
public class StudentSentFormDTO {
    private Integer templateId;
    private Integer statusId;
    private String templateTitle;

    public StudentSentFormDTO(Integer templateId, Integer statusId, String templateTitle) {
        this.templateId = templateId;
        this.statusId = statusId;
        this.templateTitle = templateTitle;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getTemplateTitle() {
        return templateTitle;
    }
}