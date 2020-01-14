package dev.codesupport.web.common.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class InternalServiceExceptionTest {

    @Test
    public void shouldCreateRuntimeExceptionSubtype() {
        String exceptionMessage = "Exception Message";

        InternalServiceException exception = new InternalServiceException(exceptionMessage);

        //ConstantConditions - never know till you check!
        //noinspection ConstantConditions
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void shouldCorrectlySetExceptionMessage() {
        String exceptionMessage = "Exception Message";

        InternalServiceException exception = new InternalServiceException(exceptionMessage);

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void shouldCorrectlySetExceptionReason() {
        Throwable mockThrowable = mock(Throwable.class);

        InternalServiceException exception = new InternalServiceException(InternalServiceException.Reason.INTERNAL, mockThrowable);

        assertEquals("Internal service issue", exception.getMessage());
    }

    @Test
    public void shouldCorrectlySetExceptionCause() {
        Throwable mockThrowable = mock(Throwable.class);

        InternalServiceException exception = new InternalServiceException(InternalServiceException.Reason.INTERNAL, mockThrowable);

        assertEquals(mockThrowable, exception.getCause());
    }

}
