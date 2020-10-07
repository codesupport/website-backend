package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.PublishedArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublishedArticleRepository extends JpaRepository<PublishedArticleEntity, Long> {

    Optional<PublishedArticleEntity> findByArticleCode(String articleCode);

}
