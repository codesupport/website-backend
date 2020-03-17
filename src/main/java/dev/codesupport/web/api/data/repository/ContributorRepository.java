package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.ContributorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributorRepository extends JpaRepository<ContributorEntity, Long> {

}
