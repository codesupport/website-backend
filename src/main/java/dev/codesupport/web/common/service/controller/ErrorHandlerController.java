package dev.codesupport.web.common.service.controller;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.common.exception.ErrorControllerException;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.controller.throwparser.ThrowableParserFactory;
import dev.codesupport.web.common.service.http.DontWrapResponse;
import lombok.extern.slf4j.Slf4j;
import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.common.service.service.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Nullable;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collections;

/**
 * Controller for handling exceptions and servlet errors.
 */
@Slf4j
@Controller
@ApiIgnore
public class ErrorHandlerController implements ErrorController {

    /**
     * Factory class used to find an appropriate parser for a given exception.
     */
    private final ThrowableParserFactory throwableParserFactory;
    /**
     * The attribute name for Spring's DefaultErrorAttribute related exceptions.
     */
    private static final String SPRING_DEFAULT_ERROR = DefaultErrorAttributes.class.getName() + ".ERROR";

    @Autowired
    public ErrorHandlerController(ThrowableParserFactory throwableParserFactory) {
        this.throwableParserFactory = throwableParserFactory;
    }

    /**
     * Handles the error/exception that caused the controller to be activated.
     * <p>This method checks to see what caused the error, whether it was caused by a servlet error with an HttpStatus
     * code, or an exception thrown.
     *
     * Any exception is then parsed by way of an appropriate parser created by the {@link ThrowableParserFactory} and
     * an appropriate message is wrapped in a {@link RestResponse} and provided back in the Http response.</p>
     *
     * @param request The data associated to the request that err'd
     * @return The wrapped response to inform the user of the error.
     */
    //WeakerAccess - SonarQube wants it public
    //S3752 - Need to use RequestMapping to catch errors for every request method type.
    @SuppressWarnings({"squid:S3752", "WeakerAccess"})
    @RequestMapping("/error")
    @DontWrapResponse
    public ResponseEntity<RestResponse<Serializable>> handleError(HttpServletRequest request) {
        RestResponse<Serializable> restResponse = createRestResponse();

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        HttpStatus httpStatus = (status instanceof Integer) ? HttpStatus.valueOf((Integer) status) : HttpStatus.I_AM_A_TEAPOT;

        Throwable throwable = getExceptionOrReturnNull(request);

        if (throwable != null) {
            //RawTypes - Don't need to specify types for this to work correctly.
            //noinspection rawtypes
            AbstractThrowableParser throwableParser = throwableParserFactory.createParser(throwable);
            throwableParser.modifyResponse(restResponse);
        } else {
            if (HttpStatus.NOT_FOUND.equals(httpStatus)) {
                restResponse.setStatus(RestStatus.NOT_FOUND);
                restResponse.setMessage(
                        Collections.singletonList("The requested endpoint does not exist.")
                );
            } else if (HttpStatus.UNAUTHORIZED.equals(httpStatus)) {
                restResponse.setStatus(RestStatus.UNAUTHORIZED);
                restResponse.setMessage(
                        Collections.singletonList("You are not authorized for this resource.")
                );
            }
        }

        if (throwable != null && !RestStatus.NOT_FOUND.equals(restResponse.getStatus())) {
            log.error("Service exception: [refId: " + restResponse.getReferenceId() + "]", throwable);
        }

        httpStatus = updateHttpStatus(httpStatus, throwable);

        if (log.isDebugEnabled()) {
            Object requestedUri = request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
            log.debug("HttpStatusCode: {}, Method: {}, URI: {}", httpStatus, request.getMethod(), requestedUri.toString());
        }

        return new ResponseEntity<>(restResponse, httpStatus);
    }

    /**
     * Searches various attributes for Exceptions, returning the first one found.
     *
     * @param request The HttpServletRequest containing possible exceptions.
     * @return Throwable exception if found, null otherwise.
     */
    @Nullable
    @VisibleForTesting
    Throwable getExceptionOrReturnNull(HttpServletRequest request) {
        Object throwable = request.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);

        if (!(throwable instanceof Throwable)) {
            throwable = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        }

        if (!(throwable instanceof Throwable)) {
            throwable = request.getAttribute(SPRING_DEFAULT_ERROR);
        }

        return (throwable instanceof Throwable) ? (Throwable) throwable : null;
    }

    /**
     * Creates a new instance of a RestResponse
     * <p>Segregated out into a method for easier unit testing.</p>
     *
     * @return A new instance of a {@link RestResponse} wrapper object.
     */
    @VisibleForTesting
    RestResponse<Serializable> createRestResponse() {
        return new RestResponse<>();
    }

    @VisibleForTesting
    HttpStatus updateHttpStatus(final HttpStatus httpStatus, Throwable throwable) {
        Throwable reference = throwable;
        HttpStatus newStatus = httpStatus;

        while (reference != null) {
            if (reference instanceof ErrorControllerException) {
                newStatus = ((ErrorControllerException)reference).getHttpStatus();
                reference = null;
            } else {
                reference = reference.getCause();
            }
        }

        return newStatus;
    }

    /**
     * Boilerplate spring method.
     *
     * @return The endpoint associated to the error page.
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
