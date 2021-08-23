package dev.codesupport.web.domain;

import dev.codesupport.web.common.domain.AbstractValidatable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * Used for returning basic User Profile information.
 * No additional permissions
 * No password.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserProfile extends AbstractValidatable<Long> {

    private Long id;
    private String alias;
    private String email;
    private Long discordId;
    private String avatarLink;
    private boolean disabled;
    private Role role;
    private String biography;
    private String gitUrl;
    private Country country;
    private Set<UserAward> userAward;
    private Long joinDate;

}
