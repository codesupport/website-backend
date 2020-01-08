package dev.codesupport.web.common.exception;

/**
 * Used to indicate service layer business logic issues.
 */
public class ServiceLayerException extends RuntimeException {
    public enum Reason {
        RESOURCE_ALREADY_EXISTS("Resource already exists."),
        EMPTY_PAYLOAD("Payload can not be an empty list.");

        private final String message;

        Reason(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    ServiceLayerException(String message) {
        super(message);
    }

    public ServiceLayerException(Reason reason) {
        super(reason.toString());
    }

    public ServiceLayerException(Reason reason, Throwable throwable) {
        super(reason.toString(), throwable);
    }
}
