package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.domain.OkResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides a simple healthcheck endpoint to check that the service is running.
 */
@RestController
@Api(value = "Healthcheck", description = "Healthcheck endpoint", tags = {"Healthcheck"})
public class HealthCheckController {

    @ApiOperation("Validate health of application")
    @GetMapping("/healthcheck")
    public OkResponse getHealthCheck() {
        return new OkResponse();
    }

}
