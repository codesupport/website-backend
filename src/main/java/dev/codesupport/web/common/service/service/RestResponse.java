package dev.codesupport.web.common.service.service;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class RestResponse<T extends Serializable> {

    private RestStatus status;
    private List<T> response;
    private String message;
    private String referenceId;

    public RestResponse() {
        setStatus(RestStatus.OK);
        setReferenceId(generateReferenceId());
    }

    public RestResponse(List<T> response) {
        this();
        setResponse(response);
    }

    public static <R extends Serializable> RestResponse<R> restResponse(List<R> response) {
        return new RestResponse<>(response);
    }

    private String generateReferenceId() {
        return System.currentTimeMillis() + "-" + RandomStringUtils.randomAlphanumeric(5);
    }

}
