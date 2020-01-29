package dev.codesupport.web.domain;

import dev.codesupport.web.common.domain.AbstractValidatable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * Used for returning User Profile information.
 * No additional permissions
 * No password.
 * No email.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserProfileStripped extends AbstractValidatable<Long> {

    private Long id;
    private String alias;
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
