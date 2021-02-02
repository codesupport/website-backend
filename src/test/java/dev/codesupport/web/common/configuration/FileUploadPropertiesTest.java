package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.exception.ConfigurationException;
import org.junit.Test;

public class FileUploadPropertiesTest {

    @Test(expected = ConfigurationException.class)
    public void shouldThrowConfigurationExceptionIfMissingProtocol() {
        FileUploadProperties properties = new FileUploadProperties();
        properties.setHostName("localhost");
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowConfigurationExceptionIfInvalidUrl() {
        FileUploadProperties properties = new FileUploadProperties();
        properties.setHostName("http:localhost");
    }

    //DefaultAnnotationParam - Just being explicit about expectation
    @SuppressWarnings("DefaultAnnotationParam")
    @Test(expected = Test.None.class)
    public void shouldNotThrowExceptionForValidHostname() {
        FileUploadProperties properties = new FileUploadProperties();
        properties.setHostName("http://example.com");
    }

    //DefaultAnnotationParam - Just being explicit about expectation
    @SuppressWarnings("DefaultAnnotationParam")
    @Test(expected = Test.None.class)
    public void shouldNotThrowExceptionForValidLocalhostHostname() {
        FileUploadProperties properties = new FileUploadProperties();
        properties.setHostName("http://localhost");
    }

}
