package dev.codesupport.web.common.exception;

/**
 * Thrown for user authentication errors.
 */
public class InvalidUserException extends RuntimeException {
    public enum Reason {
        MISSING_USER("User does not exist with the given email"),
        INVALID_USER("Invalid user credentials");

        private final String message;

        Reason(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    public InvalidUserException(Reason reason) {
        super(reason.toString());
    }

    public InvalidUserException(Reason reason, Throwable throwable) {
        super(reason.toString(), throwable);
    }
}
