package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class RequestRejectedExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        RequestRejectedExceptionParser parser = new RequestRejectedExceptionParser();

        AbstractThrowableParser<RequestRejectedException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof RequestRejectedExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        RequestRejectedExceptionParser parser = new RequestRejectedExceptionParser();

        AbstractThrowableParser<RequestRejectedException> firstInstance = parser.instantiate();
        AbstractThrowableParser<RequestRejectedException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        RequestRejectedExceptionParser parser = new RequestRejectedExceptionParser();

        List<String> expected = Collections.singletonList("The requested resource does not exist.");
        List<String> actual = parser.responseMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        RequestRejectedExceptionParser parser = new RequestRejectedExceptionParser();

        RestStatus expected = RestStatus.NOT_FOUND;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatusCode() {
        RequestRejectedExceptionParser parser = new RequestRejectedExceptionParser();

        int expected = 404;
        int actual = parser.responseCode();

        assertEquals(expected, actual);
    }

}
