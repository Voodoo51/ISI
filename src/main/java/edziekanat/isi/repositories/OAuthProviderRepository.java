package edziekanat.isi.repositories;

import edziekanat.isi.models.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthProviderRepository extends JpaRepository<OAuthProvider, Integer> {
    Optional<OAuthProvider> findByName(String name);
}
