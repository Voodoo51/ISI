package edziekanat.isi.models;

import jakarta.persistence.*;


@Entity
@Table(
        name = "oauth_user",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"provider_id", "provider_user_id"}
                )
        }
)
public class OAuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name="provider_id", nullable = false)
    private OAuthProvider provider;
    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;
    private String token;

    public OAuthUser() {
    }

    public OAuthUser(User user, OAuthProvider provider, String providerUserId, String token) {
        this.user = user;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OAuthProvider getProvider() {
        return provider;
    }

    public void setProvider(OAuthProvider provider) {
        this.provider = provider;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
