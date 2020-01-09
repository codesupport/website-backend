package dev.codesupport.web.common.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Used to create JsonWebTokens
 * <p>Using this factory will allow the {@link JwtUtility} to be automatically injected.</p>
 */
@Component
public class JsonWebTokenFactory {

    private final JwtUtility jwtUtility;

    @Autowired
    public JsonWebTokenFactory(
            JwtUtility jwtUtility
    ) {
        this.jwtUtility = jwtUtility;
    }

    /**
     * Creates a {@link JsonWebToken}
     * <p>Creates a {@link JsonWebToken}, preinjected with the current {@link JwtUtility} implementation.</p>
     *
     * @param jwtString The JWT string
     * @return The {@link JsonWebToken}
     */
    public JsonWebToken createToken(String jwtString) {
        return new JsonWebToken(jwtUtility, jwtString);
    }

}
