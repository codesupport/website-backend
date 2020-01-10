package dev.codesupport.web.common.security.models;

import dev.codesupport.web.domain.validation.annotation.EmailConstraint;
import dev.codesupport.web.domain.validation.annotation.PasswordConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Model for incoming authorization requests.
 */
@Data
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {

    @EmailConstraint
    private String email;
    @PasswordConstraint
    private String password;

}