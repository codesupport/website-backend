package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.ContributorListEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributorListRepository extends CrudRepository<ContributorListEntity, Long> {

}
