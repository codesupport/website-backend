package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.DuplicateEntryException;
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

public class DuplicateEntryExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        DuplicateEntryExceptionParser parser = new DuplicateEntryExceptionParser();

        AbstractThrowableParser<DuplicateEntryException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof DuplicateEntryExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        DuplicateEntryExceptionParser parser = new DuplicateEntryExceptionParser();

        AbstractThrowableParser<DuplicateEntryException> firstInstance = parser.instantiate();
        AbstractThrowableParser<DuplicateEntryException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        String message = "exception message";

        DuplicateEntryExceptionParser parser = new DuplicateEntryExceptionParser();

        DuplicateEntryException mockException = mock(DuplicateEntryException.class);

        //ResultOfMethodCallIgnored - We're not calling a method, we're making a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(message)
                .when(mockException)
                .getMessage();

        ReflectionTestUtils.setField(parser, "throwable", mockException);

        List<String> expected = Collections.singletonList(message);
        List<String> actual = parser.responseMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        DuplicateEntryExceptionParser parser = new DuplicateEntryExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatusCode() {
        DuplicateEntryExceptionParser parser = new DuplicateEntryExceptionParser();

        int expected = 409;
        int actual = parser.responseCode();

        assertEquals(expected, actual);
    }
}
