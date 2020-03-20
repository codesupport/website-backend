package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;

import java.util.Set;

/**
 * Used for returning basic User Profile information.
 * No additional permissions.
 * No password.
 */
@Data
public class UserProfile implements IdentifiableDomain<Long> {

    private Long id;
    private String alias;
    private String email;
    private Long discordId;
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
