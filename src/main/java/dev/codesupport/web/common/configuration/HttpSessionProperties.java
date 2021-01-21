package dev.codesupport.web.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * Configurations for Http session
 * <p>This is populated automatically at startup by spring via the application.yml files.</p>
 */
@Data
@Configuration
// This sets the class to use values from the application.yml files.
@ConfigurationProperties(prefix = "security.http")
public class HttpSessionProperties {

    CookieConfiguration cookie;
    CorsConfiguration cors;

    @Data
    public static class CookieConfiguration {

        private String name;
        private int maxAge;

    }

    @Data
    public static class CorsConfiguration {

        private Set<String> origins;
        private Set<String> methods;

    }

}
