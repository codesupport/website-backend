package dev.codesupport.web.common.security;

import dev.codesupport.web.common.exception.InvalidTokenException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Class for working with JWTs
 * <p>Handles the validation and interaction with JWTs</p>
 */
@Data
public class JsonWebToken {

    private final String username;
    private final String email;
    private final Long expiration;
    private final String issuer;

    JsonWebToken(JwtUtility jwtUtility, String jwtString) {
        if (!StringUtils.isEmpty(jwtString)) {
            JwtUtility.DecodedJWT decodedJWT = jwtUtility.decode(jwtString);

            // Store values to be accessible later.
            issuer = decodedJWT.getIssuer();
            username = decodedJWT.getUsername();
            email = decodedJWT.getEmail();
            expiration = decodedJWT.getExpiration();

            // If any of these are null, this is a bad token.
            if (getUsername() == null ||
                    getEmail() == null ||
                    getExpiration() == null ||
                    getIssuer() == null) {
                throw new InvalidTokenException(InvalidTokenException.Reason.INVALID);
            }
        } else {
            // If the token is null or empty, it's a bad token.
            throw new InvalidTokenException(InvalidTokenException.Reason.MISSING);
        }
    }

}
