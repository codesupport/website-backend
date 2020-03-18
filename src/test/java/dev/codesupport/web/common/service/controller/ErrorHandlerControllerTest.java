package dev.codesupport.web.common.service.controller;

import dev.codesupport.web.common.service.controller.throwparser.ThrowableParserFactory;
import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.testutils.controller.throwparsing.parser.ThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

//S1192 - Unit Tests are not meant to be DRY
@SuppressWarnings("squid:S1192")
public class ErrorHandlerControllerTest {

    @Test
    public void shouldReturnCorrect404ResponseForPageNotFound() {
        String referenceId = "123";

        ErrorHandlerController controllerSpy = spy(new ErrorHandlerController(mock(ThrowableParserFactory.class)));

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
                .when(controllerSpy)
                .createRestResponse();

        doReturn(httpStatus.value())
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        //ThrowableNotThrown - Not throwing an exception, mocking the return of one.
        //noinspection ThrowableNotThrown
        doReturn(null)
                .when(controllerSpy)
                .getExceptionOrReturnNull(mockRequest);

        doReturn(null)
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        RestResponse<Serializable> restResponse = new RestResponse<>();
        restResponse.setReferenceId(referenceId);
        restResponse.setStatus(RestStatus.NOT_FOUND);
        restResponse.setMessage("The requested endpoint does not exist.");

        ResponseEntity<RestResponse<Serializable>> expected = new ResponseEntity<>(restResponse, httpStatus);
        ResponseEntity<RestResponse<Serializable>> actual = controllerSpy.handleError(mockRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrect401ResponseForUnauthorizedRequest() {
        String referenceId = "123";

        ErrorHandlerController controllerSpy = spy(new ErrorHandlerController(mock(ThrowableParserFactory.class)));

        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

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
                .when(controllerSpy)
                .createRestResponse();

        doReturn(httpStatus.value())
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        //ThrowableNotThrown - Not throwing an exception, mocking the return of one.
        //noinspection ThrowableNotThrown
        doReturn(null)
                .when(controllerSpy)
                .getExceptionOrReturnNull(mockRequest);

        doReturn(null)
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        RestResponse<Serializable> restResponse = new RestResponse<>();
        restResponse.setReferenceId(referenceId);
        restResponse.setStatus(RestStatus.UNAUTHORIZED);
        restResponse.setMessage("You are not authorized for this resource.");

        ResponseEntity<RestResponse<Serializable>> expected = new ResponseEntity<>(restResponse, httpStatus);
        ResponseEntity<RestResponse<Serializable>> actual = controllerSpy.handleError(mockRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectResponseIfExceptionFound() {
        String referenceId = "123";

        ThrowableParserFactory mockThrowableParserFactory = mock(ThrowableParserFactory.class);

        ErrorHandlerController controllerSpy = spy(new ErrorHandlerController(mockThrowableParserFactory));

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
                .when(controllerSpy)
                .createRestResponse();

        doReturn(httpStatus.value())
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        //ThrowableNotThrown - Not throwing an exception, mocking the return of one.
        //noinspection ThrowableNotThrown
        doReturn(mockThrowable)
                .when(controllerSpy)
                .getExceptionOrReturnNull(mockRequest);

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
        ResponseEntity<RestResponse<Serializable>> actual = controllerSpy.handleError(mockRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectThrowableForRequestDispatcherException() {
        ThrowableParserFactory mockThrowableParserFactory = mock(ThrowableParserFactory.class);

        ErrorHandlerController controller = spy(new ErrorHandlerController(mockThrowableParserFactory));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        Throwable mockThrowable1 = mock(Throwable.class);
        Throwable mockThrowable2 = mock(Throwable.class);
        Throwable mockThrowable3 = mock(Throwable.class);

        doReturn(mockThrowable1)
                .when(mockRequest)
                .getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);

        doReturn(mockThrowable2)
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        doReturn(mockThrowable3)
                .when(mockRequest)
                .getAttribute(DefaultErrorAttributes.class.getName() + ".ERROR");

        Throwable actual = controller.getExceptionOrReturnNull(mockRequest);

        assertEquals(mockThrowable1, actual);
    }

    @Test
    public void shouldReturnCorrectThrowableForDispatcherServletException() {
        ThrowableParserFactory mockThrowableParserFactory = mock(ThrowableParserFactory.class);

        ErrorHandlerController controller = spy(new ErrorHandlerController(mockThrowableParserFactory));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        Throwable mockThrowable1 = mock(Throwable.class);
        Throwable mockThrowable2 = mock(Throwable.class);

        doReturn(null)
                .when(mockRequest)
                .getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);

        doReturn(mockThrowable1)
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        doReturn(mockThrowable2)
                .when(mockRequest)
                .getAttribute(DefaultErrorAttributes.class.getName() + ".ERROR");

        Throwable actual = controller.getExceptionOrReturnNull(mockRequest);

        assertEquals(mockThrowable1, actual);
    }

    @Test
    public void shouldReturnCorrectThrowableForDefaultAttributesError() {
        ThrowableParserFactory mockThrowableParserFactory = mock(ThrowableParserFactory.class);

        ErrorHandlerController controller = spy(new ErrorHandlerController(mockThrowableParserFactory));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        Throwable mockThrowable1 = mock(Throwable.class);

        doReturn(null)
                .when(mockRequest)
                .getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);

        doReturn(null)
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        doReturn(mockThrowable1)
                .when(mockRequest)
                .getAttribute(DefaultErrorAttributes.class.getName() + ".ERROR");

        Throwable actual = controller.getExceptionOrReturnNull(mockRequest);

        assertEquals(mockThrowable1, actual);
    }

    @Test
    public void shouldReturnNullIfNoExceptionFound() {
        ThrowableParserFactory mockThrowableParserFactory = mock(ThrowableParserFactory.class);

        ErrorHandlerController controller = spy(new ErrorHandlerController(mockThrowableParserFactory));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        doReturn(null)
                .when(mockRequest)
                .getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);

        doReturn(null)
                .when(mockRequest)
                .getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        doReturn(null)
                .when(mockRequest)
                .getAttribute(DefaultErrorAttributes.class.getName() + ".ERROR");

        Throwable actual = controller.getExceptionOrReturnNull(mockRequest);

        assertNull(actual);
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
