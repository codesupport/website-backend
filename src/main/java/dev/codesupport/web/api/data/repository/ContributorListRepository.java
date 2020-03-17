package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.ContributorListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributorListRepository extends JpaRepository<ContributorListEntity, Long> {

}
