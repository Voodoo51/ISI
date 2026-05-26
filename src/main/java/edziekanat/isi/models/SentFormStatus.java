package edziekanat.isi.models;

import jakarta.persistence.*;

@Entity
@Table(name="sent_form_status")
public class SentFormStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    public SentFormStatus() {
    }

    public SentFormStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
