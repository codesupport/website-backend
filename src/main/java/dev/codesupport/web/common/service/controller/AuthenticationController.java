package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.security.models.AuthenticationRequest;
import dev.codesupport.web.common.service.http.DontWrapResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller interface for defining mappings and annotations.
 * <p>Spring requires this to be an interface to work.</p>
 */
@RestController
@Api(value = "Authentication", description = "REST API for Authentications", tags = {"Authentication"})
@Validated
@RequestMapping("/authenticate")
public interface AuthenticationController {

    @DontWrapResponse
    @ApiOperation("Authenticate user credentials")
    @PostMapping
    ResponseEntity<Void> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest);

    @DontWrapResponse
    @ApiOperation("Refresh user token")
    @GetMapping("/refresh")
    ResponseEntity<Void> refreshToken();

    @DontWrapResponse
    @ApiOperation("Authenticate and link discord account")
    @GetMapping("/discord")
    ResponseEntity<Void> linkDiscord(@RequestParam String code);

}