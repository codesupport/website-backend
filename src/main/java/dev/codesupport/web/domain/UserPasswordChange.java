package dev.codesupport.web.domain;

import dev.codesupport.web.domain.validation.annotation.PasswordConstraint;
import lombok.Data;

import java.io.Serializable;

/**
 * Used for user registration post requests.
 */
@Data
public class UserPasswordChange implements Serializable {

    private Long id;
    @PasswordConstraint
    private String password;
    private String resetToken;

}
