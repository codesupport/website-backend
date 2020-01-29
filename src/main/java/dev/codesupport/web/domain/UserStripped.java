package dev.codesupport.web.domain;

import dev.codesupport.web.common.domain.AbstractValidatable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * Used for returning a stripped version of user details.
 * No profile info
 * No password
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserStripped extends AbstractValidatable<Long> {

    private Long id;
    private String alias;
    private Long discordId;
    private String avatarLink;
    private boolean disabled;
    private Role role;
    private Set<Permission> permission;
    private Long joinDate;

}