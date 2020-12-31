package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.FileTooBigException;
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

public class FileTooBigExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        FileTooBigExceptionParser parser = new FileTooBigExceptionParser();

        AbstractThrowableParser<FileTooBigException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof FileTooBigExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        FileTooBigExceptionParser parser = new FileTooBigExceptionParser();

        AbstractThrowableParser<FileTooBigException> firstInstance = parser.instantiate();
        AbstractThrowableParser<FileTooBigException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        String message = "exception message";

        FileTooBigExceptionParser parser = new FileTooBigExceptionParser();

        FileTooBigException mockException = mock(FileTooBigException.class);

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
        FileTooBigExceptionParser parser = new FileTooBigExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatusCode() {
        FileTooBigExceptionParser parser = new FileTooBigExceptionParser();

        int expected = 413;
        int actual = parser.responseCode();

        assertEquals(expected, actual);
    }
}
