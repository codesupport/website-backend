package dev.codesupport.web.common.exception;

/**
 * Thrown for JWT validation errors
 */
public class InvalidTokenException extends RuntimeException {
    public enum Reason {
        MISSING("Token not defined."),
        INVALID("Token is invalid."),
        EXPIRED("Token is expired.");

        private final String message;

        Reason(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    public InvalidTokenException(Reason reason) {
        super(reason.toString());
    }

    public InvalidTokenException(Reason reason, Throwable throwable) {
        super(reason.toString(), throwable);
    }
}
