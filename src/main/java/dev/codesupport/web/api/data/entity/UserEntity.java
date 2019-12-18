package dev.codesupport.web.api.data.entity;

import dev.codesupport.web.common.service.data.entity.IdentifiableEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

/**
 * API contract with the persistent storage for the {@link dev.codesupport.web.domain.User} resource.
 */
@Data
@Entity
public class UserEntity implements IdentifiableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    private String avatarLink;
    @Column(nullable = false, updatable = false)
    private Long addedBy;
    @Column(nullable = false, updatable = false)
    private Long joinDate;

    //unused - Called by hibernate under the covers
    @SuppressWarnings("unused")
    @PrePersist
    private void prePersistLogic() {
        joinDate = System.currentTimeMillis();
    }

}
