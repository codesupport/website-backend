package dev.codesupport.web.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ErrorControllerException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus;

    protected ErrorControllerException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    protected ErrorControllerException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
