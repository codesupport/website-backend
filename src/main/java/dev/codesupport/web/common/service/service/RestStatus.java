package dev.codesupport.web.common.service.service;

/**
 * The success status of the executed request.
 */
public enum RestStatus {
    /**
     * Request was successful
     */
    OK,
    /**
     * Request failed
     */
    FAIL,
    /**
     * The request succeeded, but with a warning
     */
    WARNING,
    /**
     * The request failed due to a resource not found
     */
    NOT_FOUND
}
