package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class BadCredentialsExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        BadCredentialsExceptionParser parser = new BadCredentialsExceptionParser();

        AbstractThrowableParser<BadCredentialsException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof BadCredentialsExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        BadCredentialsExceptionParser parser = new BadCredentialsExceptionParser();

        AbstractThrowableParser<BadCredentialsException> firstInstance = parser.instantiate();
        AbstractThrowableParser<BadCredentialsException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        BadCredentialsExceptionParser parser = new BadCredentialsExceptionParser();

        BadCredentialsException mockException = mock(BadCredentialsException.class);

        ReflectionTestUtils.setField(parser, "throwable", mockException);

        List<String> expected = Collections.singletonList("The username/password supplied was invalid/inactive");
        List<String> actual = parser.responseMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        BadCredentialsExceptionParser parser = new BadCredentialsExceptionParser();

        RestStatus expected = RestStatus.UNAUTHORIZED;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }
}
