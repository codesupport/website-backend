package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class HttpRequestMethodNotSupportedExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        HttpRequestMethodNotSupportedExceptionParser parser = new HttpRequestMethodNotSupportedExceptionParser();

        AbstractThrowableParser<HttpRequestMethodNotSupportedException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof HttpRequestMethodNotSupportedExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        HttpRequestMethodNotSupportedExceptionParser parser = new HttpRequestMethodNotSupportedExceptionParser();

        AbstractThrowableParser<HttpRequestMethodNotSupportedException> firstInstance = parser.instantiate();
        AbstractThrowableParser<HttpRequestMethodNotSupportedException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        HttpRequestMethodNotSupportedExceptionParser parser = new HttpRequestMethodNotSupportedExceptionParser();

        List<String> expected = Collections.singletonList("Request method not supported for this endpoint.");
        List<String> actual = parser.responseMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        HttpRequestMethodNotSupportedExceptionParser parser = new HttpRequestMethodNotSupportedExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatusCode() {
        HttpRequestMethodNotSupportedExceptionParser parser = new HttpRequestMethodNotSupportedExceptionParser();

        int expected = 405;
        int actual = parser.responseCode();

        assertEquals(expected, actual);
    }
}
