package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.security.AuthorizationService;
import dev.codesupport.web.common.security.models.AuthenticationRequest;
import dev.codesupport.web.domain.OkResponse;
import dev.codesupport.web.domain.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Accepts post requests containing username/password credentials
 * to then be validated, resulting in returned JWT if found valid.
 */
@Component
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthorizationService authorizationService;

    @Autowired
    public AuthenticationControllerImpl(
            AuthorizationService authorizationService
    ) {
        this.authorizationService = authorizationService;
    }

    /**
     * Handles authentication post requests
     * <p>Accepts posts with a {@link AuthenticationRequest} body, authenticating the credentials and
     * returning a JWT if valid.
     * If authentication fails, an exception is thrown, bubbling back up and resulting in a call
     * to the {@link ErrorHandlerController}</p>
     *
     * @param authenticationRequest The {@link AuthenticationRequest} body
     * @return The {@link TokenResponse}, with encoded JWT and associated user information
     */
    @Override
    public TokenResponse authenticate(AuthenticationRequest authenticationRequest) {
        String email = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();

        return authorizationService.createTokenForEmailAndPassword(email, password);
    }

    /**
     * Handles token refresh requests.
     * <p>If the request comes from a user with a valid JWT, it will return a new JWT.</p>
     *
     * @return The {@link TokenResponse}, with encoded JWT and associated user information
     */
    @Override
    public TokenResponse refreshToken() {
        return authorizationService.refreshToken();
    }

    /**
     * Link the discord id associated with the given code
     * <p>Attempts to acquire a Discord Id associated with the given code and link it to
     * the user authentication currently set in the Security Context</p>
     *
     * @param code The code associated to the discord user
     * @return Returns simple OK - 200 response if successful
     */
    @Override
    public OkResponse linkDiscord(String code) {
        authorizationService.linkDiscord(code);

        return new OkResponse();
    }

}