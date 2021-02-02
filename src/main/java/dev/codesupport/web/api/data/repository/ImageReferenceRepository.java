package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.ImageReferenceEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;

import java.util.Set;

public interface ImageReferenceRepository extends CrudRepository<ImageReferenceEntity, Long> {

    Set<ImageReferenceEntity> findAllByArticleId(Long articleId);

}
