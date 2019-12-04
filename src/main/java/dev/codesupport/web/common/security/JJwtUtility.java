package dev.codesupport.web.common.security;

import dev.codesupport.web.common.configuration.JwtConfiguration;
import dev.codesupport.web.common.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * io.jsonwebtoken implementation of the {@link JwtUtility} class
 * <p>Defines the logic of the {@link JwtUtility} api calls using the io.jsonwebtoken library</p>
 *
 * @see JwtUtility
 */
@Component
public class JJwtUtility extends JwtUtility {

    // Sets a unique signature key for this instance.
    // This is used to sign tokens so they can't be forged.
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Autowired
    public JJwtUtility(JwtConfiguration jwtConfiguration) {
        super(jwtConfiguration);
    }

    /**
     * Generates a new token string.
     * <p>Using the given username, and pre-configured expiration, this method creates a new token string.</p>
     *
     * @param username The username to use in the token
     * @return The encoded token string
     * @see JwtConfiguration
     */
    @Override
    public String generateToken(String username) {
        return Jwts.builder()
                .setIssuer(configuration().getIssuer())
                .claim(USERNAME, username)
                .claim(EXPIRATION, calculateExpiration(configuration().getExpiration()))
                .signWith(KEY)
                .compact();
    }

    /**
     * Decodes a token string to a {@link dev.codesupport.web.common.security.JwtUtility.DecodedJWT}
     * <p>Validates and decodes a token string.
     * Throws an exception if the token is a bad format, expired, or otherwise invalid.</p>
     *
     * @param tokenString The token string to decode.
     * @return The {@link dev.codesupport.web.common.security.JwtUtility.DecodedJWT}
     * @throws InvalidTokenException Thrown if any token validations fail.
     * @see dev.codesupport.web.common.security.JwtUtility.DecodedJWT
     * @see DecodedJJWT
     */
    @Override
    DecodedJWT decode(String tokenString) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = new DecodedJJWT(
                    Jwts.parser()
                            .setSigningKey(KEY)
                            .parseClaimsJws(tokenString)
            );

            if (decodedJWT.getExpiration() != null &&
                    decodedJWT.getExpiration() <= System.currentTimeMillis()) {
                throw new InvalidTokenException(InvalidTokenException.Reason.EXPIRED);
            }
            // Catch all expected validation exceptions, and rethrow InvalidTokenException for simplicity.
        } catch (ExpiredJwtException |
                UnsupportedJwtException |
                MalformedJwtException |
                SignatureException |
                IllegalArgumentException e) {
            throw new InvalidTokenException(InvalidTokenException.Reason.INVALID, e);
        }

        return decodedJWT;
    }

    /**
     * {@link JJwtUtility} specific implementation for {@link dev.codesupport.web.common.security.JwtUtility.DecodedJWT}
     * <p>Handles the io.jsonwebtoken library specific logic for the
     * {@link dev.codesupport.web.common.security.JwtUtility.DecodedJWT} api methods</p>
     */
    private static class DecodedJJWT implements DecodedJWT {

        private final Jws<Claims> jwsClaims;

        DecodedJJWT(Jws<Claims> jwsClaims) {
            this.jwsClaims = jwsClaims;
        }

        @Override
        public String getUsername() {
            return jwsClaims.getBody().get(USERNAME, String.class);
        }

        @Override
        public Long getExpiration() {
            return jwsClaims.getBody().get(EXPIRATION, Long.class);
        }

        @Override
        public String getIssuer() {
            return jwsClaims.getBody().getIssuer();
        }
    }

}
