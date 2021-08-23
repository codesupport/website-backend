package dev.codesupport.web.common.exception;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResourceNotFoundExceptionTest {

    @Test
    public void shouldCreateRuntimeExceptionSubtype() {
        ResourceNotFoundException exception = new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);

        //ConstantConditions - never know till you check!
        //noinspection ConstantConditions
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void shouldCorrectlySetExceptionMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);

        assertEquals(ResourceNotFoundException.Reason.NOT_FOUND.toString(), exception.getMessage());
    }

}
