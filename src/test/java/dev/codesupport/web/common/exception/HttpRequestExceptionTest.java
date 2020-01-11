package dev.codesupport.web.common.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HttpRequestExceptionTest {

    @Test
    public void shouldCreateRuntimeExceptionSubtype() {
        HttpRequestException exception = new HttpRequestException("Test");

        //ConstantConditions - never know till you check!
        //noinspection ConstantConditions
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void shouldCorrectlySetExceptionMessage() {
        String message = "Test";

        HttpRequestException exception = new HttpRequestException(message);

        assertEquals(message, exception.getMessage());
    }

}
