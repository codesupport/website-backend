package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.TagSetToTagEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagSetToTagsRepository extends CrudRepository<TagSetToTagEntity, Long> {

    List<TagSetToTagEntity> findAllByTagSetIdOrderByTagId(Long id);

}
