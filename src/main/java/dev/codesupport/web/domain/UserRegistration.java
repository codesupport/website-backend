package dev.codesupport.web.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import dev.codesupport.web.domain.validation.annotation.AliasConstraint;
import dev.codesupport.web.domain.validation.annotation.EmailConstraint;
import dev.codesupport.web.domain.validation.annotation.PasswordConstraint;
import lombok.Data;

/**
 * Used for user registration post requests.
 */
@Data
public class UserRegistration implements IdentifiableDomain<Long> {

    private Long id;
    @AliasConstraint
    private String alias;
    @PasswordConstraint
    private String password;
    @EmailConstraint
    private String email;

}
