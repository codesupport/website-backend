package dev.codesupport.web.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown for user authentication errors.
 */
public class InvalidUserException extends ErrorControllerException {
    public enum Reason {
        MISSING_USER("User does not exist with the given email", HttpStatus.UNAUTHORIZED),
        INVALID_USER("Invalid user credentials", HttpStatus.UNAUTHORIZED);

        private final String message;
        private final HttpStatus httpStatus;

        Reason(String message, HttpStatus httpStatus) {
            this.message = message;
            this.httpStatus = httpStatus;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    public InvalidUserException(Reason reason) {
        super(reason.toString(), reason.httpStatus);
    }

    public InvalidUserException(Reason reason, Throwable throwable) {
        super(reason.toString(), throwable, reason.httpStatus);
    }
}
