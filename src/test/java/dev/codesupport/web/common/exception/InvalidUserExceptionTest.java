package dev.codesupport.web.common.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class InvalidUserExceptionTest {

    @Test
    public void shouldCreateRuntimeExceptionSubtype() {
        String exceptionMessage = "Exception Message";

        InvalidUserException exception = new InvalidUserException(exceptionMessage);

        //ConstantConditions - never know till you check!
        //noinspection ConstantConditions
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void shouldCorrectlySetExceptionMessage() {
        String exceptionMessage = "Exception Message";

        InvalidUserException exception = new InvalidUserException(exceptionMessage);

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void shouldAlsoCorrectlySetExceptionMessage() {
        String exceptionMessage = "Exception Message";

        Throwable mockThrowable = mock(Throwable.class);

        InvalidUserException exception = new InvalidUserException(exceptionMessage, mockThrowable);

        assertEquals(exceptionMessage, exception.getMessage());
    }

}
