package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.security.models.AuthenticationRequest;
import dev.codesupport.web.common.security.AuthorizationService;
import dev.codesupport.web.common.service.service.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

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
    public ResponseEntity<RestResponse<String>> authenticate(AuthenticationRequest authenticationRequest) {
        String email = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();

        final String token = authorizationService.createTokenForEmailAndPassword(email, password);

        return ResponseEntity.ok(
                getRestResponse(Collections.singletonList(token))
        );
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
    public ResponseEntity<RestResponse<Serializable>> linkDiscord(String code) {
        authorizationService.linkDiscord(code);

        return ResponseEntity.ok(
                RestResponse.restResponse(Collections.singletonList("Ok"))
        );
    }

    /**
     * Returns a new instance of {@link RestResponse}
     * <p>This exists to make unit testing easier</p>
     *
     * @param objectList The resources to include in the {@link RestResponse}
     * @return The expected {@link RestResponse}
     */
    RestResponse<String> getRestResponse(List<String> objectList) {
        return new RestResponse<>(objectList);
    }

}