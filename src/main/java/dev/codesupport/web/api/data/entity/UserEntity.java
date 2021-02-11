package dev.codesupport.web.api.data.entity;

import dev.codesupport.web.common.data.entity.IdentifiableEntity;
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
 * API contract with the persistent storage for Users.
 */
@Data
@Entity
public class UserEntity implements IdentifiableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, updatable = false, length = 50, nullable = false)
    private String alias;
    @Column(length = 100, nullable = false)
    private String hashPassword;
    @Column(length = 50)
    private String verifyToken;
    @Column(length = 50)
    private String discordId;
    @Column(length = 50)
    private String discordUsername;
    @Column(length = 50)
    private String githubUsername;
    @Column(length = 50)
    private String jobTitle;
    @Column(length = 50)
    private String jobCompany;
    @Column(length = 50)
    private String accessToken;
    private Long accessTokenExpireOn;
    @Column(unique = true, length = 100, nullable = false)
    private String email;
    @Column(length = 100)
    private String avatarLink;
    @Column(nullable = false)
    private boolean disabled;
    @ManyToOne
    private RoleEntity role;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<PermissionEntity> permission;
    @Column
    private String biography;
    @ManyToOne(fetch = FetchType.EAGER)
    private CountryEntity country;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserAwardEntity> userAward;
    @Column(updatable = false, nullable = false)
    private Long joinDate;

    //unused - Called by hibernate under the covers
    @SuppressWarnings("unused")
    @PrePersist
    private void prePersistLogic() {
        joinDate = System.currentTimeMillis();
    }

}
