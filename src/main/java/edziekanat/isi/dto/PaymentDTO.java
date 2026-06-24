package edziekanat.isi.dto;

import edziekanat.isi.models.Payment;
import edziekanat.isi.models.PaymentStatus;

public class PaymentDTO {
    private Long id;
    private PaymentStatus paymentStatus;
    private String title;
    private String description;
    private Long amount;

    public PaymentDTO() {
    }

    public PaymentDTO(Long id, PaymentStatus paymentStatus, String title, String description, Long amount) {
        this.id = id;
        this.paymentStatus = paymentStatus;
        this.title = title;
        this.description = description;
        this.amount = amount;
    }

    public PaymentDTO(Payment payment) {
        this.id = payment.getId();
        this.paymentStatus = payment.getPaymentStatus();
        this.title = payment.getTitle();
        this.description = payment.getDescription();
        this.amount = payment.getAmount();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}