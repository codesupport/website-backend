package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.service.controller.throwparser.ThrowableParserFactory;
import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.testutils.controller.throwparsing.parser.ThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class ErrorHandlerControllerTest {

    @Test
    public void shouldReturnCorrect404ResponseForPageNotFound() {
        String referenceId = "123";

        ErrorHandlerController controller = spy(new ErrorHandlerController(mock(ThrowableParserFactory.class)));

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        RestResponse<Serializable> mockRestResponse = new RestResponse<>();
        mockRestResponse.setReferenceId(referenceId);

        doReturn("GET")
                .when(mockRequest)
                .getMethod();

        doReturn("test/uri")
                .when(mockRequest)
                .getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);

        doReturn(mockRestResponse)
                .when(controller)
                .createRestResponse();

        doReturn(httpStatus.value())
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        doReturn(null)
                .when(mockRequest)
                .getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);

        doReturn(null)
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        RestResponse<Serializable> restResponse = new RestResponse<>();
        restResponse.setReferenceId(referenceId);
        restResponse.setStatus(RestStatus.NOT_FOUND);
        restResponse.setMessage("The requested endpoint does not exist.");

        ResponseEntity<RestResponse<Serializable>> expected = new ResponseEntity<>(restResponse, httpStatus);
        ResponseEntity<RestResponse<Serializable>> actual = controller.handleError(mockRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResponseForRequestDispatcherException() {
        String referenceId = "123";

        ThrowableParserFactory mockThrowableParserFactory = mock(ThrowableParserFactory.class);

        ErrorHandlerController controller = spy(new ErrorHandlerController(mockThrowableParserFactory));

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        RestResponse<Serializable> mockRestResponse = new RestResponse<>();
        mockRestResponse.setReferenceId(referenceId);

        Throwable mockThrowable = mock(Throwable.class);

        doReturn("GET")
                .when(mockRequest)
                .getMethod();

        doReturn("test/uri")
                .when(mockRequest)
                .getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);

        doReturn(mockRestResponse)
                .when(controller)
                .createRestResponse();

        doReturn(httpStatus.value())
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        doReturn(null)
                .when(mockRequest)
                .getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);

        doReturn(mockThrowable)
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        ThrowableParser throwableParser = new ThrowableParser();
        ReflectionTestUtils.setField(throwableParser, "throwable", mockThrowable);

        doReturn(throwableParser)
                .when(mockThrowableParserFactory)
                .createParser(mockThrowable);

        RestResponse<Serializable> restResponse = new RestResponse<>();
        restResponse.setReferenceId(referenceId);
        restResponse.setStatus(RestStatus.WARNING);
        restResponse.setMessage("Mock parser message");

        ResponseEntity<RestResponse<Serializable>> expected = new ResponseEntity<>(restResponse, httpStatus);
        ResponseEntity<RestResponse<Serializable>> actual = controller.handleError(mockRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResponseForDispatcherServletException() {
        String referenceId = "123";

        ThrowableParserFactory mockThrowableParserFactory = mock(ThrowableParserFactory.class);

        ErrorHandlerController controller = spy(new ErrorHandlerController(mockThrowableParserFactory));

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        RestResponse<Serializable> mockRestResponse = new RestResponse<>();
        mockRestResponse.setReferenceId(referenceId);

        Throwable mockThrowable = mock(Throwable.class);

        doReturn("GET")
                .when(mockRequest)
                .getMethod();

        doReturn("test/uri")
                .when(mockRequest)
                .getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);

        doReturn(mockRestResponse)
                .when(controller)
                .createRestResponse();

        doReturn(httpStatus.value())
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        doReturn(mockThrowable)
                .when(mockRequest)
                .getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);

        doReturn(null)
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        ThrowableParser throwableParser = new ThrowableParser();
        ReflectionTestUtils.setField(throwableParser, "throwable", mockThrowable);

        doReturn(throwableParser)
                .when(mockThrowableParserFactory)
                .createParser(mockThrowable);

        RestResponse<Serializable> restResponse = new RestResponse<>();
        restResponse.setReferenceId(referenceId);
        restResponse.setStatus(RestStatus.WARNING);
        restResponse.setMessage("Mock parser message");

        ResponseEntity<RestResponse<Serializable>> expected = new ResponseEntity<>(restResponse, httpStatus);
        ResponseEntity<RestResponse<Serializable>> actual = controller.handleError(mockRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCreateNewInstanceOfRestResponse() {
        ErrorHandlerController controller = new ErrorHandlerController(mock(ThrowableParserFactory.class));

        RestResponse<Serializable> firstInstance = controller.createRestResponse();
        RestResponse<Serializable> secondInstance = controller.createRestResponse();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectErrorPath() {
        ErrorHandlerController controller = new ErrorHandlerController(mock(ThrowableParserFactory.class));

        assertEquals("/error", controller.getErrorPath());
    }

}
