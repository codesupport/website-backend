package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.service.service.RestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

import static dev.codesupport.web.common.service.service.RestResponse.restResponse;

/**
 * Provides a simple healthcheck endpoint to check that the service is running.
 */
@RestController
public class HealthcheckController {

    @GetMapping("/health")
    public RestResponse<Serializable> getHealthCheck(){
        return restResponse(null);
    }

}
