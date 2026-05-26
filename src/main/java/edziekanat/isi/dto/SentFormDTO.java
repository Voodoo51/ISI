package edziekanat.isi.dto;

import edziekanat.isi.models.SentForm;

//change the DTO appendix to something different
public class SentFormDTO {
    private long id;
    private long templateId;
    private int statusId;
    private String title;

    public SentFormDTO(SentForm sentForm) {
        this.id = sentForm.getId();
        this.templateId = sentForm.getFormTemplate().getId();
        this.statusId = sentForm.getStatus().getId();
        this.title = sentForm.getFormTemplate().getTitle();
    }

    public SentFormDTO(long id, long templateId, int statusId, String title) {
        this.id = id;
        this.templateId = templateId;
        this.statusId = statusId;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
