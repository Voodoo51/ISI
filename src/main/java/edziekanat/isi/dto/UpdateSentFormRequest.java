package edziekanat.isi.dto;

public class UpdateSentFormRequest {
    private Long sentFormId;
    private Integer newStatusId;
    private String response;

    public UpdateSentFormRequest() {
    }

    public UpdateSentFormRequest(Long sentFormId, Integer newStatusId, String response) {
        this.sentFormId = sentFormId;
        this.newStatusId = newStatusId;
        this.response = response;
    }

    public Long getSentFormId() {
        return sentFormId;
    }

    public void setSentFormId(Long sentFormId) {
        this.sentFormId = sentFormId;
    }

    public Integer getNewStatusId() {
        return newStatusId;
    }

    public void setNewStatusId(Integer newStatusId) {
        this.newStatusId = newStatusId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
