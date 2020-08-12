package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    List<TagEntity> findAllByLabelInOrderByLabel(List<String> labels);

}
