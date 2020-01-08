package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.service.service.CrudOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the application.
 */
@Configuration
class ApplicationConfiguration {

    /**
     * Performs configurations requires for the application to function.
     *
     * This is automatically found and executed by Spring.
     *
     * @param context Spring's ApplicationContext
     */
    @Autowired
    ApplicationConfiguration(
            ApplicationContext context
    ) {
        CrudOperations.setContext(context);
    }

}