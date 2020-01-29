package dev.codesupport.web.common.service.service;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * A wrapper to give additional information associated with the request along with the desired resource
 *
 * @param <T> The serializable datatype of the resource to be returned as part of the response
 */
@Data
public class RestResponse<T extends Serializable> {

    /**
     * The success status of the request
     *
     * @see RestStatus
     */
    private RestStatus status;
    /**
     * The desired resource of the request
     */
    private List<T> response;
    /**
     * Any error/warning messages associated to the specific request
     */
    private String message;
    /**
     * A reference ID to associate to the specific request, used for logging / troubleshooting
     */
    private String referenceId;

    /**
     * Automatically sets {@link RestStatus} and referenceId
     */
    public RestResponse() {
        setStatus(RestStatus.OK);
        setReferenceId(generateReferenceId());
    }

    /**
     * Adds the provided resource list and automatically sets {@link RestStatus} and referenceId
     *
     * @param response The resource list to add to the object
     */
    public RestResponse(List<T> response) {
        this();
        setResponse(response);
    }

    /**
     * Adds the provided resource as a list and automatically sets {@link RestStatus} and referenceId
     *
     * @param response The resource to add to the object
     */
    public RestResponse(T response) {
        this(Collections.singletonList(response));
    }

    /**
     * Shortcut static method to generate a rest response
     * <p></p>
     *
     * @param response The resulting resource list following execution of the request
     * @param <R>      The serializable resource type associated with the request
     * @return a new {@link RestResponse} object, populated with the associated resource.
     */
    public static <R extends Serializable> RestResponse<R> restResponse(List<R> response) {
        return new RestResponse<>(response);
    }

    /**
     * Shortcut static method to generate a rest response
     * <p></p>
     *
     * @param response The resulting resource list following execution of the request
     * @param <R>      The serializable resource type associated with the request
     * @return a new {@link RestResponse} object, populated with the associated resource.
     */
    public static <R extends Serializable> RestResponse<R> restResponse(R response) {
        return new RestResponse<>(response);
    }

    /**
     * Generates a random referenceId to associate to the request/response
     *
     * @return a random referenceId, prepended with the time of the request in EPOCH
     */
    private String generateReferenceId() {
        return System.currentTimeMillis() + "-" + RandomStringUtils.randomAlphanumeric(5);
    }

}
