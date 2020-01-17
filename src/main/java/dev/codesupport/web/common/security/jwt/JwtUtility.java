package dev.codesupport.web.common.security.jwt;

import dev.codesupport.web.common.configuration.JwtProperties;

/**
 * Abstract utility to define implementation of specific utility.
 * <p>Abstract class for utilizing a third party library or custom implementation for
 * JWT logic.</p>
 */
public abstract class JwtUtility {

    private JwtProperties jwtProperties;

    /**
     * JWT property name for expiration
     */
    protected static final String EXPIRATION = "expiration";
    /**
     * JWT property name for username
     */
    protected static final String USERNAME = "username";
    /**
     * JWT property name for email
     */
    protected static final String EMAIL = "email";

    public JwtUtility(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * Gets the {@link JwtProperties}
     *
     * @return The stored {@link JwtProperties}
     */
    protected JwtProperties configuration() {
        return jwtProperties;
    }

    public abstract String generateToken(String username, String email);

    abstract DecodedJWT decode(String tokenString);

    /**
     * Calculates an expiration time
     * <p>Gets the current system time (ms) and adds the timeToExpire to find the expiration time.</p>
     *
     * @param timeToExpire Time until expiration (ms)
     * @return The time of the expiration.
     */
    protected Long calculateExpiration(Long timeToExpire) {
        return System.currentTimeMillis() + timeToExpire;
    }

    /**
     * Decoded JWT
     * <p>This is how you actually pull data from a JWT after it's been decoded.
     * This will be implemented by the JwtUtility implementation class.</p>
     */
    protected interface DecodedJWT {

        String getUsername();

        String getEmail();

        Long getExpiration();

        String getIssuer();

    }

}
