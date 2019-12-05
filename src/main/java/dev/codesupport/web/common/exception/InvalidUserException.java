package dev.codesupport.web.common.exception;

/**
 * Thrown for user authentication errors.
 */
public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String message) {
        super(message);
    }

    public InvalidUserException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
