package edziekanat.isi.dto;

public class PaymentRequest {
    private Long id;

    public PaymentRequest() {
    }

    public PaymentRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
