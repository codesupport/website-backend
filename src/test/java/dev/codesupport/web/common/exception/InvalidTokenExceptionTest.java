package dev.codesupport.web.common.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class InvalidTokenExceptionTest {

    @Test
    public void shouldCreateRuntimeExceptionSubtype() {
        InvalidTokenException exception = new InvalidTokenException(InvalidTokenException.Reason.INVALID);

        //ConstantConditions - never know till you check!
        //noinspection ConstantConditions
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void shouldCorrectlySetExceptionMessage() {
        InvalidTokenException exception = new InvalidTokenException(InvalidTokenException.Reason.INVALID);

        assertEquals(InvalidTokenException.Reason.INVALID.toString(), exception.getMessage());
    }

    @Test
    public void shouldAlsoCorrectlySetExceptionMessage() {
        Throwable mockThrowable = mock(Throwable.class);

        InvalidTokenException exception = new InvalidTokenException(InvalidTokenException.Reason.INVALID, mockThrowable);

        assertEquals(InvalidTokenException.Reason.INVALID.toString(), exception.getMessage());
    }

}
