package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.security.models.AuthenticationRequest;
import dev.codesupport.web.domain.OkResponse;
import dev.codesupport.web.domain.TokenResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("Authenticate user credentials")
    @PostMapping
    TokenResponse authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest);

    @ApiOperation("Refresh user token")
    @GetMapping("/refresh")
    TokenResponse refreshToken();

    @ApiOperation("Authenticate and link discord account")
    @GetMapping("/discord")
    OkResponse linkDiscord(@RequestParam String code);

}