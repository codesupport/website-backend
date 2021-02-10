package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.TagSetToTagEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TagSetToTagsRepository extends CrudRepository<TagSetToTagEntity, Long> {

    Set<TagSetToTagEntity> findAllByTagSetId(Long id);

}
