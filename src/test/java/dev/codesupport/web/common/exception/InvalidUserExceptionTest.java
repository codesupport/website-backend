package dev.codesupport.web.common.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class InvalidUserExceptionTest {

    @Test
    public void shouldCreateRuntimeExceptionSubtype() {
        InvalidUserException exception = new InvalidUserException(InvalidUserException.Reason.MISSING_USER);

        //ConstantConditions - never know till you check!
        //noinspection ConstantConditions
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void shouldCorrectlySetExceptionMessageForMissingUser() {
        InvalidUserException exception = new InvalidUserException(InvalidUserException.Reason.MISSING_USER);

        assertEquals(InvalidUserException.Reason.MISSING_USER.toString(), exception.getMessage());
    }

    @Test
    public void shouldCorrectlySetExceptionMessageForInvalidUser() {
        InvalidUserException exception = new InvalidUserException(InvalidUserException.Reason.INVALID_USER);

        assertEquals(InvalidUserException.Reason.INVALID_USER.toString(), exception.getMessage());
    }

    @Test
    public void shouldAlsoCorrectlySetExceptionMessage() {
        Throwable mockThrowable = mock(Throwable.class);

        InvalidUserException exception = new InvalidUserException(InvalidUserException.Reason.MISSING_USER, mockThrowable);

        assertEquals(InvalidUserException.Reason.MISSING_USER.toString(), exception.getMessage());
    }

    @Test
    public void shouldCorrectlySetCause() {
        Throwable mockThrowable = mock(Throwable.class);

        InvalidUserException exception = new InvalidUserException(InvalidUserException.Reason.MISSING_USER, mockThrowable);

        assertSame(mockThrowable, exception.getCause());
    }

}
