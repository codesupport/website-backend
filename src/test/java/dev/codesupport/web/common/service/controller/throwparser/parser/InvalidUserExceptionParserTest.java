package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class InvalidUserExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        InvalidUserExceptionParser parser = new InvalidUserExceptionParser();

        AbstractThrowableParser<InvalidUserException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof InvalidUserExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        InvalidUserExceptionParser parser = new InvalidUserExceptionParser();

        AbstractThrowableParser<InvalidUserException> firstInstance = parser.instantiate();
        AbstractThrowableParser<InvalidUserException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        String exceptionMessage = "Invalid user exception message";

        InvalidUserExceptionParser parser = new InvalidUserExceptionParser();

        InvalidUserException mockException = mock(InvalidUserException.class);

        doReturn(exceptionMessage)
                .when(mockException)
                .getMessage();

        ReflectionTestUtils.setField(parser, "throwable", mockException);

        String actual = parser.responseMessage();

        assertEquals(exceptionMessage, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        InvalidUserExceptionParser parser = new InvalidUserExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }
}
