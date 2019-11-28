package dev.codesupport.web.common.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigurationExceptionTest {

    @Test
    public void shouldCreateRuntimeExceptionSubtype() {
        String exceptionMessage = "Exception Message";

        ConfigurationException exception = new ConfigurationException(exceptionMessage);

        //ConstantConditions - never know till you check!
        //noinspection ConstantConditions
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void shouldCorrectlySetExceptionMessage() {
        String exceptionMessage = "Exception Message";

        ConfigurationException exception = new ConfigurationException(exceptionMessage);

        assertEquals(exceptionMessage, exception.getMessage());
    }

}
