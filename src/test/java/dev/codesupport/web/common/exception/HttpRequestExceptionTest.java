package dev.codesupport.web.common.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HttpRequestExceptionTest {

    @Test
    public void shouldCreateRuntimeExceptionSubtype() {
        String exceptionMessage = "Exception Message";

        HttpRequestException exception = new HttpRequestException(exceptionMessage);

        //ConstantConditions - never know till you check!
        //noinspection ConstantConditions
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void shouldCorrectlySetExceptionMessage() {
        String exceptionMessage = "Exception Message";

        HttpRequestException exception = new HttpRequestException(exceptionMessage);

        assertEquals(exceptionMessage, exception.getMessage());
    }

}
