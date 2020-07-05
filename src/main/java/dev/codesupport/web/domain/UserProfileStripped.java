package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;

import java.util.Set;

/**
 * Used for returning User Profile information.
 * No additional permissions
 * No password.
 * No email.
 */
@Data
public class UserProfileStripped implements IdentifiableDomain<Long> {

    private Long id;
    private String alias;
    private String discordId;
    private String discordUsername;
    private String githubUsername;
    private String jobTitle;
    private String jobCompany;
    private String avatarLink;
    private boolean disabled;
    private Role role;
    private String biography;
    private Country country;
    private Set<UserAward> userAward;
    private Long joinDate;

}
