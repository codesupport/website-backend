package dev.codesupport.web.common.exception;

/**
 * Used to indicate that a authenticating user is disabled.
 */
public class DisabledUserException extends RuntimeException {
    public DisabledUserException(String message) {
        super(message);
    }
}
