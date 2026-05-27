package edziekanat.isi.dto;

import edziekanat.isi.models.Payments;
import edziekanat.isi.models.User;

public class PaymentsDto {
    private User user;
    private float amount;

    public PaymentsDto(Payments payments) {
        this.user = payments.getUserId();
        this.amount = payments.getAmount();
    }


    public User getId() {
        return user;
    }

    public void setId(long id) {
        this.user = user;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}