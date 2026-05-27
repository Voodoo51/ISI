package edziekanat.isi.models;

import jakarta.persistence.*;

@Entity
@Table(name="payments")
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Long amount;
    private String status;

    public Payments(){}

    public Payments(long id, User user,  Long amount, String status) {
        this.id = id;
        this.user= user;
        this.amount = amount;
        this.status = status;
    }

    public Long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public User getUser() {  return user; }

    public void setUser(User userId) { this.user = user; }

    public Long getAmount() { return amount;  }

    public void setAmount(Long amount) { this.amount = amount; }

    public String getStatus() {  return status; }

    public void setStatus(String status) {  this.status = status; }
}
