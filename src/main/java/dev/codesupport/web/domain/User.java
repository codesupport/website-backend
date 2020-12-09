package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.Data;

/**
 * Used internally by service to perform actions on a User.  Not to be returned by API.
 */
@Data
public class User implements IdentifiableDomain<Long> {

    private Long id;
    private String alias;
    private String email;
    private String discordId;
    private String discordUsername;
    private String avatarLink;
    private boolean disabled;
    private Role role;
    private Long joinDate;

}