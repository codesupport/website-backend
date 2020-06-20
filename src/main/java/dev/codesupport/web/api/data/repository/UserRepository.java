package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data Access Layer for the persistent storage related to the {@link UserEntity} resource.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByAliasIgnoreCase(String alias);

    boolean existsByEmailIgnoreCase(String email);

    UserEntity findByEmailIgnoreCase(String email);

    UserEntity findByAliasIgnoreCase(String alias);

}
