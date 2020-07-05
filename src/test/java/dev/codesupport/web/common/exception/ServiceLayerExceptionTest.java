package dev.codesupport.web.common.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ServiceLayerExceptionTest {

    @Test
    public void shouldCreateErrorControllerExceptionSubtype() {
        ServiceLayerException exception = new ServiceLayerException(ServiceLayerException.Reason.RESOURCE_ALREADY_EXISTS);

        //ConstantConditions - never know till you check!
        //noinspection ConstantConditions
        assertTrue(exception instanceof ErrorControllerException);
    }

    @Test
    public void shouldCorrectlySetExceptionMessage() {
        ServiceLayerException exception = new ServiceLayerException(ServiceLayerException.Reason.RESOURCE_ALREADY_EXISTS);

        assertEquals(ServiceLayerException.Reason.RESOURCE_ALREADY_EXISTS.toString(), exception.getMessage());
    }

    @Test
    public void shouldAlsoCorrectlySetExceptionMessage() {
        Throwable mockThrowable = mock(Throwable.class);

        ServiceLayerException exception = new ServiceLayerException(ServiceLayerException.Reason.RESOURCE_ALREADY_EXISTS, mockThrowable);

        assertEquals(ServiceLayerException.Reason.RESOURCE_ALREADY_EXISTS.toString(), exception.getMessage());
    }

    @Test
    public void shouldCorrectlySetCause() {
        Throwable mockThrowable = mock(Throwable.class);

        ServiceLayerException exception = new ServiceLayerException(ServiceLayerException.Reason.RESOURCE_ALREADY_EXISTS, mockThrowable);

        assertSame(mockThrowable, exception.getCause());
    }

}
