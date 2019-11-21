package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.controller.throwparser.ThrowableParserFactory;
import lombok.extern.slf4j.Slf4j;
import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.common.service.service.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Slf4j
@Controller
public class ErrorHandlerController implements ErrorController {

    private final ThrowableParserFactory throwableParserFactory;

    @Autowired
    ErrorHandlerController(ThrowableParserFactory throwableParserFactory) {
        this.throwableParserFactory = throwableParserFactory;
    }

    //WeakerAccess - SonarQube wants it public
    //S3752 - Need to use RequestMapping to catch errors for every request method type.
    @SuppressWarnings({"squid:S3752", "WeakerAccess"})
    @RequestMapping("/error")
    public ResponseEntity<RestResponse<Serializable>> handleError(HttpServletRequest request) {
        RestResponse<Serializable> restResponse = createRestResponse();

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        HttpStatus httpStatus = (status instanceof Integer) ? HttpStatus.valueOf((Integer) status) : HttpStatus.I_AM_A_TEAPOT;

        Object exception = request.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);

        if (!(exception instanceof Throwable)) {
            exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        }

        if (exception instanceof Throwable) {
            AbstractThrowableParser throwableParser = throwableParserFactory.createParser((Throwable) exception);
            throwableParser.modifyResponse(restResponse);
        } else {
            if (HttpStatus.NOT_FOUND.equals(httpStatus)) {
                restResponse.setStatus(RestStatus.NOT_FOUND);
                restResponse.setMessage("The requested endpoint does not exist.");
            }
        }

        if (exception instanceof Throwable && !RestStatus.NOT_FOUND.equals(restResponse.getStatus())) {
            log.error("Service exception: [refId: " + restResponse.getReferenceId() + "]", (Throwable) exception);
        }

        if (RestStatus.NOT_FOUND.equals(restResponse.getStatus())) {
            httpStatus = HttpStatus.NOT_FOUND;
        }

        if (log.isDebugEnabled()) {
            Object requestedUri = request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
            log.debug("HttpStatusCode: {}, Method: {}, URI: {}", httpStatus, request.getMethod(), requestedUri.toString());
        }

        return new ResponseEntity<>(restResponse, httpStatus);
    }

    RestResponse<Serializable> createRestResponse() {
        return new RestResponse<>();
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
