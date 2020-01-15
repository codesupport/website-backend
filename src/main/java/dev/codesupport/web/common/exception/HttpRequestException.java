package dev.codesupport.web.common.exception;

/**
 * Thrown to denote an issue with an Http Request.
 */
public class HttpRequestException extends RuntimeException {
    public HttpRequestException(String message) {
        super(message);
    }
}
