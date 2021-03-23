package dev.codesupport.web.common.configuration;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.util.ValidationUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Configurations for Discord App
 * <p>This is populated automatically at startup by spring via the application.yml files.</p>
 */
@Data
@Configuration
// This sets the class to use values from the application.yml files.
@ConfigurationProperties(prefix = "security.discord")
public class DiscordAppProperties {

    private String apiHost;
    private String clientId;
    private String secret;
    private String redirectUri;

    @PostConstruct
    public void init() {
        validate();
    }

    public void setApiHost(String apiHost) {
        this.apiHost = StringUtils.stripEnd(apiHost, "/");
    }

    /**
     * Determines if the properties were property set.
     */

    @VisibleForTesting
    void validate() {
        if (!isValid()) {
            throw new ConfigurationException("Discord app properties not set.");
        }
    }

    public boolean isValid() {
        return ValidationUtils.isValidUrl(apiHost) &&
                !StringUtils.isEmpty(clientId) &&
                !StringUtils.isEmpty(secret) &&
                !StringUtils.isEmpty(redirectUri);
    }

}
