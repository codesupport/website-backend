package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.TagEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TagRepository extends CrudRepository<TagEntity, Long> {

    Set<TagEntity> findAllByLabelIn(Set<String> labels);

}
