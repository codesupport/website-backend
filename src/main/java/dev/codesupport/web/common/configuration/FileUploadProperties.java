package dev.codesupport.web.common.configuration;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.util.ValidationUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Configurations for file uploads
 * <p>This is populated automatically at startup by spring via the application.yml files.</p>
 */
@Setter
@EqualsAndHashCode
@Configuration
// This sets the class to use values from the application.yml files.
@ConfigurationProperties(prefix = "upload")
public class FileUploadProperties {

    @Getter
    private String hostName;

    /**
     * Properties for image files
     */
    private Image image;

    @PostConstruct
    public void init() {
        validate();
    }

    public Image imageProperties() {
        return image;
    }

    public void setHostName(String hostName) {
        if (hostName != null) {
            if (!hostName.startsWith("http")) {
                throw new ConfigurationException("Host name must include protocol");
            }

            if (!ValidationUtils.isValidUrl(hostName)) {
                throw new ConfigurationException("Host name must be a valid url");
            }

            this.hostName = hostName;
        }
    }

    @VisibleForTesting
    void validate() {
        if (StringUtils.isBlank(hostName)) {
            throw new ConfigurationException("Host name configuration not set");
        }
    }

    @Data
    public static class Image {

        /**
         * Image upload size limit in Bytes
         */
        private Long maxSize;

    }

}
