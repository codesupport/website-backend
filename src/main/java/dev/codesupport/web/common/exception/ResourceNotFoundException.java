package dev.codesupport.web.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Used to indicate a given resource was not found in the persistent storage.
 *
 * @see dev.codesupport.web.common.service.service.CrudOperations
 */
public class ResourceNotFoundException extends ErrorControllerException {
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
        super(reason.toString(), HttpStatus.NOT_FOUND);
    }
}
