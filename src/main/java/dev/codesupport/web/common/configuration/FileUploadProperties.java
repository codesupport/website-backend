package dev.codesupport.web.common.configuration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configurations for JWTs
 * <p>This is populated automatically at startup by spring via the application.yml files.</p>
 */
@Setter
@EqualsAndHashCode
@Configuration
// This sets the class to use values from the application.yml files.
@ConfigurationProperties(prefix = "upload")
public class FileUploadProperties {

    /**
     * Properties for image files
     */
    private Image image;

    public Image imageProperties() {
        return image;
    }

    @Data
    public static class Image {

        /**
         * Image upload size limit in Bytes
         */
        private Long maxSize;

    }

}
