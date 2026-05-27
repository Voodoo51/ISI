package edziekanat.isi.repositories;

import edziekanat.isi.models.OAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthUserRepository  extends JpaRepository<OAuthUser, Long> {
    Optional<OAuthUser> findByProviderUserId(String id);

}
