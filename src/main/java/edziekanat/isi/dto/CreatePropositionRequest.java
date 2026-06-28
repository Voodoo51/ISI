package edziekanat.isi.dto;

public class CreatePropositionRequest {
    private Long userId;
    private String title;
    private String description;

    public CreatePropositionRequest() {
    }

    public CreatePropositionRequest(Long userId, String title, String description) {
        this.userId = userId;
        this.title = title;
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}