package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * Used internally by service to perform actions on a User.  Not to be returned by API.
 */
@Data
@FieldNameConstants
public class NewUser implements IdentifiableDomain<Long> {
    private Long id;
    private String alias;
    private String password;
    private String hashPassword;
    private String email;
    private String discordId;
    private String discordUsername;
    private String avatarLink;
    private boolean disabled;
    private Role role;
    private Long joinDate;
}