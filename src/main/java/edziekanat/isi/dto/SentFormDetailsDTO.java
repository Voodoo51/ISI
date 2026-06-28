package edziekanat.isi.dto;

import java.time.LocalDateTime;

public class SentFormDetailsDTO {
    private Long id;
    private Integer templateId;
    private String templateTitle;
    private LocalDateTime sentAt;
    private String response;
    private UserPublicData user;
    private Integer statusId;

    public SentFormDetailsDTO() {
    }

    public SentFormDetailsDTO(Long id, Integer templateId, String templateTitle, LocalDateTime sentAt, String response, UserPublicData user, Integer statusId) {
        this.id = id;
        this.templateId = templateId;
        this.templateTitle = templateTitle;
        this.sentAt = sentAt;
        this.response = response;
        this.user = user;
        this.statusId = statusId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getTemplateTitle() {
        return templateTitle;
    }

    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public UserPublicData getUser() {
        return user;
    }

    public void setUser(UserPublicData user) {
        this.user = user;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
}
