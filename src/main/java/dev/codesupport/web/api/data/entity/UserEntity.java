package dev.codesupport.web.api.data.entity;

import dev.codesupport.web.common.service.data.entity.IdentifiableEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import java.util.Set;

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
    private String alias;
    @Column(nullable = false)
    private String hashPassword;
    private String discordId;
    private String discordUsername;
    private String githubUsername;
    private String jobTitle;
    private String jobCompany;
    @Column(nullable = false)
    private String email;
    private String avatarLink;
    @Column(nullable = false)
    private boolean disabled;
    @ManyToOne
    private RoleEntity role;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<PermissionEntity> permission;
    private String biography;
    @ManyToOne(fetch = FetchType.EAGER)
    private CountryEntity country;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserAwardEntity> userAward;
    @Column(nullable = false, updatable = false)
    private Long joinDate;

    //unused - Called by hibernate under the covers
    @SuppressWarnings("unused")
    @PrePersist
    private void prePersistLogic() {
        joinDate = System.currentTimeMillis();
    }

}
