package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends CrudRepository<ArticleEntity, Long> {

    List<ArticleEntity> findAllByRevisionIdNotNull();
    boolean existsByTitleId(String title);

}
