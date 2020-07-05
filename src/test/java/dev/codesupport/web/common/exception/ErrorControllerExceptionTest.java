package dev.codesupport.web.common.exception;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ErrorControllerExceptionTest {

    @Test
    public void shouldCreateRuntimeExceptionSubtype() {
        String exceptionMessage = "Exception Message";

        ErrorControllerException exception = new ErrorControllerException(exceptionMessage, HttpStatus.OK);

        //ConstantConditions - never know till you check!
        //noinspection ConstantConditions
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void shouldCorrectlySetExceptionMessage() {
        String exceptionMessage = "Exception Message";

        ErrorControllerException exception = new ErrorControllerException(exceptionMessage, HttpStatus.OK);

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void shouldAlsoCorrectlySetExceptionMessage() {
        String exceptionMessage = "Exception Message";

        Throwable mockThrowable = mock(Throwable.class);

        ErrorControllerException exception = new ErrorControllerException(exceptionMessage, mockThrowable, HttpStatus.OK);

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void shouldCorrectlySetExceptionCause() {
        String exceptionMessage = "Exception Message";

        Throwable mockThrowable = mock(Throwable.class);

        ErrorControllerException exception = new ErrorControllerException(exceptionMessage, mockThrowable, HttpStatus.OK);

        assertSame(mockThrowable, exception.getCause());
    }

    @Test
    public void shouldCorrectlySetHttpStatus() {
        String exceptionMessage = "Exception Message";

        ErrorControllerException exception = new ErrorControllerException(exceptionMessage, HttpStatus.OK);

        assertEquals(HttpStatus.OK, exception.getHttpStatus());
    }

    @Test
    public void shouldAlsoCorrectlySetHttpStatus() {
        String exceptionMessage = "Exception Message";

        Throwable mockThrowable = mock(Throwable.class);

        ErrorControllerException exception = new ErrorControllerException(exceptionMessage, mockThrowable, HttpStatus.OK);

        assertEquals(HttpStatus.OK, exception.getHttpStatus());
    }

}
