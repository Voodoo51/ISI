package edziekanat.isi.dto;

import jakarta.validation.constraints.*;

public class CreatePaymentRequest {

    @NotNull(message = "paymentUserEmptyError")
    private Long userId;
    @NotBlank(message = "paymentTitleEmptyError")
    private String title;
    private String description;
    @NotNull(message = "paymentAmountEmptyError")
    @Min(value = 1, message = "Amount must be at least 1")
    @Max(value = 1000, message = "Amount must be at most 1000")
    private Long amount;

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getAmount() {
        return amount;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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