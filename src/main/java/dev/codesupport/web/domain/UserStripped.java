package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;

import java.util.Set;

/**
 * Used for returning a stripped version of user details.
 * No profile info
 * No password
 */
@Data
public class UserStripped implements IdentifiableDomain<Long> {

    private Long id;
    private String alias;
    private String discordId;
    private String avatarLink;
    private boolean disabled;
    private Role role;
    private Set<Permission> permission;
    private Long joinDate;

}