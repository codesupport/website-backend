package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.ArticleRevisionEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRevisionRepository extends CrudRepository<ArticleRevisionEntity, Long> {

    List<ArticleRevisionEntity> findAllByArticleId(Long id);

}
