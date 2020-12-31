package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ServiceLayerExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        ServiceLayerExceptionParser parser = new ServiceLayerExceptionParser();

        AbstractThrowableParser<ServiceLayerException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof ServiceLayerExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        ServiceLayerExceptionParser parser = new ServiceLayerExceptionParser();

        AbstractThrowableParser<ServiceLayerException> firstInstance = parser.instantiate();
        AbstractThrowableParser<ServiceLayerException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        String exceptionMessage = "Service layer exception message";

        ServiceLayerExceptionParser parser = new ServiceLayerExceptionParser();

        ServiceLayerException mockException = mock(ServiceLayerException.class);

        //ResultOfMethodCallIgnored - We're not calling a method, we're making a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(exceptionMessage)
                .when(mockException)
                .getMessage();

        ReflectionTestUtils.setField(parser, "throwable", mockException);

        List<String> expected = Collections.singletonList(exceptionMessage);
        List<String> actual = parser.responseMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        ServiceLayerExceptionParser parser = new ServiceLayerExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }
}
