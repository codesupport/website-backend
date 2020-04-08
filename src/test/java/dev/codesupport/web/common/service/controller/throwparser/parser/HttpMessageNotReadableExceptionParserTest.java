package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class HttpMessageNotReadableExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        HttpMessageNotReadableExceptionParser parser = new HttpMessageNotReadableExceptionParser();

        AbstractThrowableParser<HttpMessageNotReadableException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof HttpMessageNotReadableExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        HttpMessageNotReadableExceptionParser parser = new HttpMessageNotReadableExceptionParser();

        AbstractThrowableParser<HttpMessageNotReadableException> firstInstance = parser.instantiate();
        AbstractThrowableParser<HttpMessageNotReadableException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        HttpMessageNotReadableExceptionParser parser = new HttpMessageNotReadableExceptionParser();

        List<String> expected = Collections.singletonList("Could not parse JSON payload.");
        List<String> actual = parser.responseMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        HttpMessageNotReadableExceptionParser parser = new HttpMessageNotReadableExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }
}
