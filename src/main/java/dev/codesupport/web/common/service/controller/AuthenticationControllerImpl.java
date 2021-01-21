package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.security.AuthenticationService;
import dev.codesupport.web.common.security.models.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Accepts post requests containing username/password credentials
 * to then be validated, resulting in returned token cookie if found valid.
 */
@Component
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationControllerImpl(
            AuthenticationService authenticationService
    ) {
        this.authenticationService = authenticationService;
    }

    /**
     * Handles authentication post requests
     * <p>Accepts posts with a {@link AuthenticationRequest} body, authenticating the credentials and
     * returning a token cookie if valid.
     * If authentication fails, an exception is thrown, bubbling back up and resulting in a call
     * to the {@link ErrorHandlerController}</p>
     *
     * @param authenticationRequest The {@link AuthenticationRequest} body
     * @return Response entity with created cookie header
     */
    @Override
    public ResponseEntity<Void> authenticate(AuthenticationRequest authenticationRequest) {
        String email = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();

        String cookieValue = authenticationService.authenticate(email, password);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieValue).body(null);
    }

    /**
     * Placeholder endpoint to refresh tokens.
     */
    @Override
    public ResponseEntity<Void> refreshToken() {
        return ResponseEntity.ok().body(null);
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
    public ResponseEntity<Void> linkDiscord(String code) {
        authenticationService.linkDiscord(code);

        return ResponseEntity.ok().body(null);
    }

}