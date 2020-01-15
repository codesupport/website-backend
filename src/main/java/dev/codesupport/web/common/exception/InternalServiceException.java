package dev.codesupport.web.common.exception;

/**
 * Thrown for internal service exceptions, used to consolidate exceptions.
 */
public class InternalServiceException extends RuntimeException {
    public enum Reason {
        INTERNAL("Internal service issue"),
        EXTERNAL("Failed to access external resource");

        private final String message;

        Reason(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    public InternalServiceException(String message) {
        super(message);
    }

    public InternalServiceException(Reason reason, Throwable throwable) {
        super(reason.toString(), throwable);
    }
}
