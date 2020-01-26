package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.security.models.AuthenticationRequest;
import dev.codesupport.web.common.service.service.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * Controller interface for defining mappings and annotations.
 * <p>Spring requires this to be an interface to work.</p>
 */
@RestController
@Validated
public interface AuthenticationController {

    @PostMapping(value = "/authenticate")
    ResponseEntity<RestResponse<String>> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest);

    @GetMapping(value = "/authenticate/discord")
    ResponseEntity<RestResponse<Serializable>> linkDiscord(@RequestParam String code);

}