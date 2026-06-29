package edziekanat.isi.repositories;

import edziekanat.isi.models.FormTemplate;
import edziekanat.isi.models.SentForm;
import edziekanat.isi.models.SentFormStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SentFormRepository extends JpaRepository<SentForm, Long>{
    List<SentForm> findByUserId(Long userId);

    @Query("""
        SELECT sf
        FROM SentForm sf
        WHERE sf.user.id = :userId
          AND (:statusId IS NULL OR sf.status.id = :statusId)
    """)
        List<SentForm> findByUserIdAndOptionalStatus(
                @Param("userId") Long userId,
                @Param("statusId") Integer statusId
        );

    Optional<SentForm> findByUserIdAndFormTemplateId(Long userId, Integer templateId);
    @Query("""
        SELECT sf
        FROM SentForm sf
        JOIN sf.formTemplate ft
        ORDER BY ft.createdAt DESC, sf.sentAt DESC
        """)
    Page<SentForm> findAllOrderByTemplateCreatedAt(Pageable pageable);

    @Query("""
        SELECT sf
        FROM SentForm sf
        WHERE (:statusId IS NULL OR sf.status.id = :statusId)
        ORDER BY sf.sentAt DESC, sf.id DESC
        """)
    Page<SentForm> findAllByStatus(
            @Param("statusId") Integer statusId,
            Pageable pageable
    );

    @Query("""
    SELECT sf
    FROM SentForm sf
    JOIN sf.formTemplate ft
    JOIN sf.user u
    WHERE (
            LOWER(ft.title) LIKE LOWER(CONCAT('%', :query, '%'))
         OR LOWER(CONCAT(u.name, ' ', u.surname)) LIKE LOWER(CONCAT('%', :query, '%'))
          )
      AND (:statusId IS NULL OR sf.status.id = :statusId)
    ORDER BY sf.sentAt DESC
""")
    Page<SentForm> search(
            @Param("query") String query,
            @Param("statusId") Integer statusId,
            Pageable pageable
    );

    @Query("""
    SELECT sf
    FROM SentForm sf
    JOIN sf.formTemplate ft
    WHERE sf.user.id = :userId
      AND (
            LOWER(ft.title) LIKE LOWER(CONCAT('%', :query, '%'))
          )
      AND (:statusId IS NULL OR sf.status.id = :statusId)
    ORDER BY sf.sentAt DESC
""")
    Page<SentForm> searchUserForms(
            @Param("userId") Long userId,
            @Param("query") String query,
            @Param("statusId") Integer statusId,
            Pageable pageable
    );
}
