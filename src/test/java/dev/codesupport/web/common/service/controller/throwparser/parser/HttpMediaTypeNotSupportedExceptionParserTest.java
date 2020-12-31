package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class HttpMediaTypeNotSupportedExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        HttpMediaTypeNotSupportedExceptionParser parser = new HttpMediaTypeNotSupportedExceptionParser();

        AbstractThrowableParser<HttpMediaTypeNotSupportedException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof HttpMediaTypeNotSupportedExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        HttpMediaTypeNotSupportedExceptionParser parser = new HttpMediaTypeNotSupportedExceptionParser();

        AbstractThrowableParser<HttpMediaTypeNotSupportedException> firstInstance = parser.instantiate();
        AbstractThrowableParser<HttpMediaTypeNotSupportedException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        String content = "content";
        String type = "type";

        HttpMediaTypeNotSupportedExceptionParser parser = new HttpMediaTypeNotSupportedExceptionParser();

        HttpMediaTypeNotSupportedException mockException = mock(HttpMediaTypeNotSupportedException.class);

        MediaType mockMediaType = mock(MediaType.class);

        doReturn(content)
                .when(mockMediaType)
                .getType();

        doReturn(type)
                .when(mockMediaType)
                .getSubtype();

        doReturn(mockMediaType)
                .when(mockException)
                .getContentType();

        ReflectionTestUtils.setField(parser, "throwable", mockException);

        List<String> expected = Collections.singletonList("Content type not supported: " + content + "/" + type);
        List<String> actual = parser.responseMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectMessageIfContentTypeNull() {
        HttpMediaTypeNotSupportedExceptionParser parser = new HttpMediaTypeNotSupportedExceptionParser();

        HttpMediaTypeNotSupportedException mockException = mock(HttpMediaTypeNotSupportedException.class);

        doReturn(null)
                .when(mockException)
                .getContentType();

        ReflectionTestUtils.setField(parser, "throwable", mockException);

        List<String> expected = Collections.singletonList("Content type not supported: ");
        List<String> actual = parser.responseMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        HttpMediaTypeNotSupportedExceptionParser parser = new HttpMediaTypeNotSupportedExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatusCode() {
        HttpMediaTypeNotSupportedExceptionParser parser = new HttpMediaTypeNotSupportedExceptionParser();

        int expected = 415;
        int actual = parser.responseCode();

        assertEquals(expected, actual);
    }

}
