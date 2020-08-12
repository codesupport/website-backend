package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.TagSetToTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagSetToTagsRepository extends JpaRepository<TagSetToTagEntity, Long> {

    List<TagSetToTagEntity> findAllByTagSetIdOrderByTagId(Long id);

}
