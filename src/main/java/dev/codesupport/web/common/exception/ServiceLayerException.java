package dev.codesupport.web.common.exception;

public class ServiceLayerException extends RuntimeException {
    public enum Reason {
        RESOURCE_ALREADY_EXISTS("Resource already exists");

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
}
