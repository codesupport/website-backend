package dev.codesupport.web.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public enum Reason {
        NOT_FOUND("Resource with the given ID was not found");

        private final String message;

        Reason(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    public ResourceNotFoundException(Reason reason) {
        super(reason.toString());
    }
}
