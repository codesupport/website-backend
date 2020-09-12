package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data Access Layer for the persistent storage related to the {@link UserEntity} resource.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByAliasIgnoreCase(String alias);

    boolean existsByEmailIgnoreCase(String email);

    Optional<UserEntity> findByEmailIgnoreCase(String email);

    Optional<UserEntity> findByAliasIgnoreCase(String alias);

}
