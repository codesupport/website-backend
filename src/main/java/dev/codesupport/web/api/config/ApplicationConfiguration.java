package dev.codesupport.web.api.config;

import dev.codesupport.web.common.service.service.CrudOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
class ApplicationConfiguration {

    @Autowired
    ApplicationConfiguration(
            ApplicationContext context
    ) {
        CrudOperations.setContext(context);
    }

}