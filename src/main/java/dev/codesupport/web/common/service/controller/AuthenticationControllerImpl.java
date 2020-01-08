package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.security.AuthenticationRequest;
import dev.codesupport.web.common.security.AuthorizationService;
import dev.codesupport.web.common.service.service.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;

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
     * @return The encoded JWT string if authentication passes.
     */
    public ResponseEntity<RestResponse<Serializable>> authenticate(AuthenticationRequest authenticationRequest) {
        String email = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();

        final String token = authorizationService.createTokenForEmailAndPassword(email, password);

        return ResponseEntity.ok(
                getRestResponse(Collections.singletonList(token))
        );
    }

}