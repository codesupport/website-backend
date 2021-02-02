package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.service.ServiceService;
import dev.codesupport.web.domain.Endpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Controller
@RequiredArgsConstructor
// Having system specific load issues, need to lazy load this bean
@Lazy
public class ServiceControllerImpl implements ServiceController {

    public final ServiceService service;

    @Override
    public Set<Endpoint> getEndpoints() {
        return service.getEndpoints();
    }

}
