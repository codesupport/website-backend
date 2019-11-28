package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class NumberFormatExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        NumberFormatExceptionParser parser = new NumberFormatExceptionParser();

        AbstractThrowableParser instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof NumberFormatExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        NumberFormatExceptionParser parser = new NumberFormatExceptionParser();

        AbstractThrowableParser firstInstance = parser.instantiate();
        AbstractThrowableParser secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        String exceptionMessage = "Number format exception message";

        NumberFormatExceptionParser parser = new NumberFormatExceptionParser();

        NumberFormatException mockException = mock(NumberFormatException.class);

        doReturn(exceptionMessage)
                .when(mockException)
                .getMessage();

        ReflectionTestUtils.setField(parser, "throwable", mockException);

        String actual = parser.responseMessage();

        assertEquals(exceptionMessage, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        NumberFormatExceptionParser parser = new NumberFormatExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }
}
