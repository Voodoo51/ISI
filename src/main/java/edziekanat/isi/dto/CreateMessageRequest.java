package edziekanat.isi.dto;

public class CreateMessageRequest {
    private Long propositionId;
    private Long userId;
    private String message;

    public CreateMessageRequest() {
    }

    public CreateMessageRequest(Long propositionId, Long userId, String message) {
        this.propositionId = propositionId;
        this.userId = userId;
        this.message = message;
    }

    public Long getPropositionId() {
        return propositionId;
    }

    public void setPropositionId(Long propositionId) {
        this.propositionId = propositionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}