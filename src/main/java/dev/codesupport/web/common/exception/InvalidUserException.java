package dev.codesupport.web.common.exception;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
