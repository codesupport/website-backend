package dev.codesupport.web.domain;

import dev.codesupport.web.common.domain.AbstractValidatable;
import dev.codesupport.web.domain.validation.annotation.AliasConstraint;
import dev.codesupport.web.domain.validation.annotation.EmailConstraint;
import dev.codesupport.web.domain.validation.annotation.PasswordConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserRegistration extends AbstractValidatable<Long> {

    private Long id;
    @AliasConstraint
    private String alias;
    @PasswordConstraint
    private String password;
    @EmailConstraint
    private String email;

}
