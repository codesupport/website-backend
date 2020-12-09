package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.PublishedArticleEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublishedArticleRepository extends CrudRepository<PublishedArticleEntity, Long> {

    Optional<PublishedArticleEntity> findByArticleCode(String articleCode);
    List<PublishedArticleEntity> findAllByPublishedIsTrue();
    boolean existsByArticleCode(String articleCode);

}
