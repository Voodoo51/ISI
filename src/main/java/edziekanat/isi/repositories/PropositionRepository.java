package edziekanat.isi.repositories;

import edziekanat.isi.models.Proposition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropositionRepository extends JpaRepository<Proposition,Long> {
    Page<Proposition> findAllByUserId(Long userId, Pageable pageable);

    @Query("""
        SELECT p
        FROM Proposition p
        JOIN p.user u
        WHERE
            LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR 
            LOWER(CONCAT(u.name, ' ', u.surname)) LIKE LOWER(CONCAT('%', :query, '%'))
        ORDER BY p.id DESC
    """)
        Page<Proposition> search(
                @Param("query") String query,
                Pageable pageable
        );

    @Query("""
            SELECT p
            FROM Proposition p
            WHERE p.user.id = :userId
            AND (
                LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            ORDER BY p.id DESC
    """)
    Page<Proposition> searchUser(
            @Param("userId") Long userId,
            @Param("query") String query,
            Pageable pageable
    );
}
