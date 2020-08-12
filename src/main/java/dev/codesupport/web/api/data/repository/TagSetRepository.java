package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.TagSetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagSetRepository extends JpaRepository<TagSetEntity, Long> {


}
