package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.TagEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<TagEntity, Long> {

    List<TagEntity> findAllByLabelInOrderByLabel(List<String> labels);

}
