package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

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

        AccessDeniedException mockException = mock(AccessDeniedException.class);

        ReflectionTestUtils.setField(parser, "throwable", mockException);

        String exceptionMessage = "You are not permitted to perform the requested action on the requested resource.";
        String actual = parser.responseMessage();

        assertEquals(exceptionMessage, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        AccessDeniedExceptionParser parser = new AccessDeniedExceptionParser();

        RestStatus expected = RestStatus.UNAUTHORIZED;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }
}
