package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.exception.ConfigurationException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Configurations for JWTs
 * <p>This is populated automatically at startup by spring via the application.yml files.</p>
 */
@Data
@Configuration
// This sets the class to use values from the application.yml files.
@ConfigurationProperties(prefix = "security.jwt")
public class JwtConfiguration {

    /**
     * The acceptable value pattern for expiration time expressions, ex:
     * 5s
     * 5m
     * 5h
     */
    private static final Pattern timePattern = Pattern.compile("^\\d+[msh]?$");
    /**
     * Unit -> coefficient map for converting time expressions to millisecond values
     */
    private static final Map<String, Integer> coefficients;

    static {
        coefficients = new HashMap<>();
        coefficients.put("s", 1000);
        coefficients.put("m", 60000);
        coefficients.put("h", 3600000);
    }

    /**
     * Issuer of the JWT
     */
    private String issuer;
    /**
     * How long until the token expires after it's been created
     */
    private Long expiration = 1000 * 60 * 5L; // 5 minutes default

    /**
     * Sets expiration
     * <p>This is how much time (in millis) until the expiration.
     * Accepts integer values followed by time unit, ex:
     * 5s
     * 5m
     * 5h</p>
     *
     * @param expiration The amount of time (ms) to expire.
     */
    public void setExpiration(String expiration) {
        String trimExpiration = expiration.trim();
        if (!StringUtils.isEmpty(trimExpiration)) {
            if (timePattern.matcher(trimExpiration).matches()) {
                Long time = Long.valueOf(StringUtils.substring(trimExpiration, 0, -1));
                time *= coefficients.get(
                        StringUtils.substring(trimExpiration, -1)
                );
                this.expiration = time;
            } else {
                throw new ConfigurationException("Invalid property format: expiration");
            }
        }
    }
}
