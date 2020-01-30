package dev.codesupport.web.domain;

import lombok.Data;
import dev.codesupport.web.common.domain.AbstractValidatable;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * Used internally by service to perform actions on a User.  Not to be returned by API.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractValidatable<Long> {

    private Long id;
    private String alias;
    private String password;
    private String hashPassword;
    private String email;
    private String avatarLink;
    private boolean disabled;
    private Role role;
    private Set<Permission> permission;
    private Long joinDate;

}