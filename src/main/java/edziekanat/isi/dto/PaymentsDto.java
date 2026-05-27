package edziekanat.isi.dto;

import edziekanat.isi.models.Payments;
import edziekanat.isi.models.User;

public class PaymentsDto {
    private Long userId;
    private Long amount;

    public PaymentsDto(Payments payments) {
        this.userId = payments.getUser().getId();
        this.amount = payments.getAmount();
    }

    public Long getId() {
        return userId;
    }

    public void setId(Long id) {
        this.userId = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}