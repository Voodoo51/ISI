package edziekanat.isi.misc;

import edziekanat.isi.models.User;
import edziekanat.isi.models.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final Long userId;
    private final String roleName;
    private final String email;
    private final String password;

    public CustomUserDetails(User user) {
        this.userId = user.getId();
        System.out.println("DEATILS ID: " + user.getId());
        this.roleName = user.getRole().getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase())
        );
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getEmail() {
        return email;
    }
}