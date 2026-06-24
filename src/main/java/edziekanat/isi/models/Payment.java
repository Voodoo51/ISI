package edziekanat.isi.models;

import jakarta.persistence.*;

@Entity
@Table(name="payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name="payment_status_id", nullable = false)
    private PaymentStatus paymentStatus;
    @Column(name="order_id", nullable = true)
    private String orderId;
    private String title;
    private String description;
    private Long amount;

    public Payment() {
    }

    public Payment(Long id, User user, PaymentStatus paymentStatus, String orderId, String title, String description, Long amount) {
        this.id = id;
        this.user = user;
        this.paymentStatus = paymentStatus;
        this.orderId = orderId;
        this.title = title;
        this.description = description;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
