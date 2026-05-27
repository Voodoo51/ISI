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

    private float amount;
    private String status;

    public Payments(){}

    public Payments(long id, User user,  float amount, String status) {
        this.id = id;
        this.user= user;
        this.amount = amount;
        this.status = status;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public User getUserId() {  return user; }

    public void setUserId(User userId) { this.user = user; }

    public float getAmount() { return amount;  }

    public void setAmount(float amount) { this.amount = amount; }

    public String getStatus() {  return status; }

    public void setStatus(String status) {  this.status = status; }
}
