package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.service.ServiceService;
import dev.codesupport.web.domain.Endpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ServiceControllerImpl implements ServiceController {

    public final ServiceService service;

    @Override
    public Set<Endpoint> getEndpoints() {
        return service.getEndpoints();
    }

}
