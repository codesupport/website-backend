package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.ShowcaseEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//S100 - Underscores are required spring repo syntax
@SuppressWarnings("squid:S100")
@Repository
public interface ShowcaseRepository extends CrudRepository<ShowcaseEntity, Long> {

    List<ShowcaseEntity> findAllByUser_IdOrderById(Long userId);

    List<ShowcaseEntity> findAllByUser_AliasIgnoreCaseOrderById(String alias);

}
