package dev.codesupport.web.common.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Model for incoming authorization requests.
 */
@Data
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {

    private static final long serialVersionUID = 4690455532399413757L;

    private String username;
    private String password;

}