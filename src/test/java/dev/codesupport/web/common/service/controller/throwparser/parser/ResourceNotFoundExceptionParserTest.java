package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ResourceNotFoundExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        ResourceNotFoundExceptionParser parser = new ResourceNotFoundExceptionParser();

        AbstractThrowableParser<ResourceNotFoundException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof ResourceNotFoundExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        ResourceNotFoundExceptionParser parser = new ResourceNotFoundExceptionParser();

        AbstractThrowableParser<ResourceNotFoundException> firstInstance = parser.instantiate();
        AbstractThrowableParser<ResourceNotFoundException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        String exceptionMessage = "Resource not found exception message";

        ResourceNotFoundExceptionParser parser = new ResourceNotFoundExceptionParser();

        ResourceNotFoundException mockException = mock(ResourceNotFoundException.class);

        doReturn(exceptionMessage)
                .when(mockException)
                .getMessage();

        ReflectionTestUtils.setField(parser, "throwable", mockException);

        String actual = parser.responseMessage();

        assertEquals(exceptionMessage, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        ResourceNotFoundExceptionParser parser = new ResourceNotFoundExceptionParser();

        RestStatus expected = RestStatus.NOT_FOUND;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }
}
