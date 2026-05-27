package edziekanat.isi.models;

import jakarta.persistence.*;

@Entity
@Table(name="oauth_provider")
public class OAuthProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;

    public OAuthProvider() {
    }

    public OAuthProvider(Integer id, String name) {
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
