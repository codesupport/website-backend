package dev.codesupport.web.common.service.controller.throwparser;

import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class DefaultExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        DefaultExceptionParser parser = new DefaultExceptionParser();

        AbstractThrowableParser instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof DefaultExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        DefaultExceptionParser parser = new DefaultExceptionParser();

        AbstractThrowableParser<Throwable> firstInstance = parser.instantiate();
        AbstractThrowableParser<Throwable> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        DefaultExceptionParser parser = new DefaultExceptionParser();

        String expected = "Server failure, exception logged.";
        String actual = parser.responseMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        DefaultExceptionParser parser = new DefaultExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = parser.responseStatus();

        assertEquals(expected, actual);
    }
}
