package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.security.AuthenticationRequest;
import dev.codesupport.web.common.service.service.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@RestController
@Validated
public interface AuthenticationController {

    @PostMapping(value = "/authenticate")
    ResponseEntity<RestResponse<Serializable>> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest);

    /**
     * Returns a new instance of {@link RestResponse}
     * <p>This exists to make unit testing easier</p>
     *
     * @param objectList The resources to include in the {@link RestResponse}
     * @return The expected {@link RestResponse}
     */
    default RestResponse<Serializable> getRestResponse(List<Serializable> objectList) {
        return new RestResponse<>(objectList);
    }

}