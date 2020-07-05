package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;

import java.util.Set;

/**
 * Used internally by service to perform actions on a User.  Not to be returned by API.
 */
@Data
public class User implements IdentifiableDomain<Long> {

    private Long id;
    private String alias;
    private String password;
    private String hashPassword;
    private String discordId;
    private String discordUsername;
    private String githubUsername;
    private String jobTitle;
    private String jobCompany;
    private String email;
    private String avatarLink;
    private boolean disabled;
    private Role role;
    private Set<Permission> permission;
    private String biography;
    private Country country;
    private Set<UserAward> userAward;
    private Long joinDate;

}