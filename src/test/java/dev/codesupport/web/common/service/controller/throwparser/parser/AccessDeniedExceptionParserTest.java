package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class AccessDeniedExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        AccessDeniedExceptionParser parser = new AccessDeniedExceptionParser();

        AbstractThrowableParser<AccessDeniedException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof AccessDeniedExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        AccessDeniedExceptionParser parser = new AccessDeniedExceptionParser();

        AbstractThrowableParser<AccessDeniedException> firstInstance = parser.instantiate();
        AbstractThrowableParser<AccessDeniedException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        AccessDeniedExceptionParser parser = new AccessDeniedExceptionParser();

        List<String> expected = Collections.singletonList(
                "You are not permitted to perform the requested action on the requested resource."
        );
        List<String> actual = parser.responseMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        AccessDeniedExceptionParser parser = new AccessDeniedExceptionParser();

        RestStatus expected = RestStatus.UNAUTHORIZED;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatusCode() {
        AccessDeniedExceptionParser parser = new AccessDeniedExceptionParser();

        int expected = 403;
        int actual = parser.responseCode();

        assertEquals(expected, actual);
    }

}
