package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.security.AuthenticationRequest;
import dev.codesupport.web.common.service.service.AuthenticationUserDetailsService;
import dev.codesupport.web.common.service.service.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @Autowired
    public AuthenticationController(
            AuthenticationManager authenticationManager,
            AuthenticationUserDetailsService userDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<RestResponse<Serializable>> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        //TODO: Create token with user details.
        final String token = "1234";
        return ResponseEntity.ok(
                getRestResponse(Collections.singletonList(token))
        );
    }

    RestResponse<Serializable> getRestResponse(List<Serializable> objectList) {
        return new RestResponse<>(objectList);
    }

    void authenticate(String username, String password) {
        try {
            // Authenticate, if no exceptions thrown then we are good.
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new InvalidUserException("Account is disabled.", e);
        }
    }
}