package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data Access Layer for the persistent storage related to the {@link UserEntity} resource.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByAlias(String alias);

    boolean existsByEmail(String email);

    UserEntity findByEmail(String email);

    UserEntity findByAlias(String alias);

}
