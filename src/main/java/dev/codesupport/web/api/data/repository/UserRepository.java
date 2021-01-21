package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.common.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data Access Layer for the persistent storage related to the {@link UserEntity} resource.
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    boolean existsByAliasIgnoreCase(String alias);

    boolean existsByEmailIgnoreCase(String email);

    Optional<UserEntity> findByEmailIgnoreCase(String email);

    Optional<UserEntity> findByAccessTokenIgnoreCase(String token);

    Optional<UserEntity> findByAliasIgnoreCase(String alias);

}
