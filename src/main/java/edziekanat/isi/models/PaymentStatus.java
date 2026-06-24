package edziekanat.isi.models;

import jakarta.persistence.*;

@Entity
@Table(name="payment_status")
public class PaymentStatus {
    @Id
    private Integer id;
    private String name;

    public PaymentStatus() {
    }

    public PaymentStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
