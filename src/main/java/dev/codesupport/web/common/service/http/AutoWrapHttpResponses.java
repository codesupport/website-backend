package dev.codesupport.web.common.service.http;

import dev.codesupport.web.common.exception.InternalServiceException;
import dev.codesupport.web.common.service.service.RestResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Intercepts objects returned from controllers and wraps them in a standard response.
 */
@ControllerAdvice
public class AutoWrapHttpResponses implements ResponseBodyAdvice<Object> {

    /**
     * Methods that should not have their return objects wrapped
     */
    private static final List<String> NON_SUPPORTED_METHODS;

    static {
        NON_SUPPORTED_METHODS = Arrays.asList(
                "uiconfiguration",
                "securityconfiguration",
                "swaggerresources",
                "getdocumentation"
        );
    }

    /**
     * Determines which return objects will be returned
     * <p>Currently configured to wrap anything that does NOT come from the error handler</p>
     *
     * @param returnType    ?
     * @param converterType The class type of the returned object
     * @return True if this wrapper supports the returned object, False otherwise
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        boolean useWrapper;

        if (returnType.getMethod() == null) {
            useWrapper = false;
        } else {
            useWrapper = returnType.getMethodAnnotation(DontWrapResponse.class) == null &&
                    !NON_SUPPORTED_METHODS.contains(
                            returnType.getMethod().getName().toLowerCase()
                    );
        }

        return useWrapper;
    }

    /**
     * Wraps the returned object with a standard response wrapper
     *
     * @param body                  The returned object from the controller
     * @param returnType            ?
     * @param selectedContentType   The media type designation for the endpoint
     * @param selectedConverterType The class type of the returned object
     * @param request               The associated http request object
     * @param response              The associated http response object
     * @return The object returned by the controller, wrapped in a standard response wrapper.
     * @throws InternalServiceException If the returned object does not implement serializable
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (!request.getURI().toString().contains("/actuator/")) {
            if (body instanceof Serializable) {
                return new RestResponse<>((Serializable) body);
            } else {
                throw new InternalServiceException("Response object was invalid type");
            }
        }

        return body;
    }

}