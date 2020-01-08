package dev.codesupport.web.domain;

import lombok.Data;
import dev.codesupport.web.common.domain.AbstractValidatable;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractValidatable<Long> {

    private Long id;
    private String alias;
    private String password;
    private String hashPassword;
    private String email;
    private Long discordId;
    private String avatarLink;
    private boolean disabled;
    private Role role;
    private Set<Permission> permission;
    private Long joinDate;

}