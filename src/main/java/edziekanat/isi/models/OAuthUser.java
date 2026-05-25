package edziekanat.isi.models;

import jakarta.persistence.*;


@Entity
@Table(name="oauth_user")
public class OAuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    private String token;
}
