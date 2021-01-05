package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.domain.Endpoint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * Controller for service information, non-business resources
 */
@RestController
@Api(value = "Service", description = "REST API for Service information", tags = {"Service"})
@Validated
@RequestMapping("/service")
public interface ServiceController {

    @ApiOperation("Get list of available endpoints")
    @GetMapping("/v1/endpoints")
    Set<Endpoint> getEndpoints();

}
