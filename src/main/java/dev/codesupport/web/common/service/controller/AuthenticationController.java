package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.security.AuthenticationRequest;
import dev.codesupport.web.common.security.JwtUtility;
import dev.codesupport.web.common.service.service.AuthenticationUserDetailsService;
import dev.codesupport.web.common.service.service.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Accepts post requests containing username/password credentials
 * to then be validated, resulting in returned JWT if found valid.
 */
@RestController
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private AuthenticationUserDetailsService userDetailsService;
    private JwtUtility jwtUtility;

    @Autowired
    public AuthenticationController(
            AuthenticationManager authenticationManager,
            AuthenticationUserDetailsService userDetailsService,
            JwtUtility jwtUtility
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtility = jwtUtility;
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
    @PostMapping(value = "/authenticate")
    public ResponseEntity<RestResponse<Serializable>> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        authenticate(username, authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(username);
        final String token = jwtUtility.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(
                getRestResponse(Collections.singletonList(token))
        );
    }

    /**
     * Returns a new instance of {@link RestResponse}
     * <p>This exists to make unit testing easier</p>
     *
     * @param objectList The resources to include in the {@link RestResponse}
     * @return The expected {@link RestResponse}
     */
    RestResponse<Serializable> getRestResponse(List<Serializable> objectList) {
        return new RestResponse<>(objectList);
    }

    /**
     * Authenticates the provided credentials
     * <p>Attempts to authenticate the provided credentials, throwing an exception is any authentication
     * logic fails.</p>
     *
     * @param username The username for the user
     * @param password The password for the user
     * @throws InvalidUserException Thrown if the user can't be authenticated.
     */
    void authenticate(String username, String password) {
        try {
            // Authenticate, if not null and no exceptions thrown then we are good.
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (authentication == null) {
                throw new InvalidUserException("User could not be authenticated.");
            }
        } catch (DisabledException e) {
            throw new InvalidUserException("Account is disabled.", e);
        }
    }
}