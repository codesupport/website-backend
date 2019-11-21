package dev.codesupport.web.common.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServiceLayerExceptionTest {

    @Test
    public void shouldCreateRuntimeExceptionSubtype() {
        ServiceLayerException exception = new ServiceLayerException(ServiceLayerException.Reason.RESOURCE_ALREADY_EXISTS);

        //ConstantConditions - never know till you check!
        //noinspection ConstantConditions
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void shouldCorrectlySetExceptionMessage() {
        ServiceLayerException exception = new ServiceLayerException(ServiceLayerException.Reason.RESOURCE_ALREADY_EXISTS);

        assertEquals(ServiceLayerException.Reason.RESOURCE_ALREADY_EXISTS.toString(), exception.getMessage());
    }

    @Test
    public void shouldAlsoCorrectlySetExceptionMessage() {
        String exceptionMessage = "Exception Message";

        ServiceLayerException exception = new ServiceLayerException(exceptionMessage);

        assertEquals(exceptionMessage, exception.getMessage());
    }

}
