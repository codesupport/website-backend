package dev.codesupport.web.common.configuration;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configurations for JWTs
 * <p>This is populated automatically at startup by spring via the application.yml files.</p>
 */
@Data
@Configuration
// This sets the class to use values from the application.yml files.
@ConfigurationProperties(prefix = "security.discord")
public class DiscordAppConfiguration {

    private String clientId;
    private String secret;
    private String redirectUri;

    public boolean isValid() {
        return !StringUtils.isEmpty(clientId) &&
                !StringUtils.isEmpty(secret) &&
                !StringUtils.isEmpty(redirectUri);
    }

}
