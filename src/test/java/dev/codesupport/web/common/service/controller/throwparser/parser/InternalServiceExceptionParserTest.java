package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.InternalServiceException;
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

public class InternalServiceExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        InternalServiceExceptionParser parser = new InternalServiceExceptionParser();

        AbstractThrowableParser<InternalServiceException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof InternalServiceExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        InternalServiceExceptionParser parser = new InternalServiceExceptionParser();

        AbstractThrowableParser<InternalServiceException> firstInstance = parser.instantiate();
        AbstractThrowableParser<InternalServiceException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        String exceptionMessage = "Service layer exception message";

        InternalServiceExceptionParser parser = new InternalServiceExceptionParser();

        InternalServiceException mockException = mock(InternalServiceException.class);

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
        InternalServiceExceptionParser parser = new InternalServiceExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }
}
