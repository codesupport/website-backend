package dev.codesupport.web.common.exception;

/**
 * Used to indicate that a particular component has not been configured.
 */
public class ConfigurationException extends RuntimeException {
    public ConfigurationException(String message) {
        super(message);
    }
}
