package edziekanat.isi.dto;

public class UpdatePaymentRequest {
    private String title;
    private String description;
    private Long amount;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getAmount() {
        return amount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
