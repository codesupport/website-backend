package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class MethodArgumentNotValidExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        MethodArgumentNotValidExceptionParser parser = new MethodArgumentNotValidExceptionParser();

        AbstractThrowableParser<MethodArgumentNotValidException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof MethodArgumentNotValidExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        MethodArgumentNotValidExceptionParser parser = new MethodArgumentNotValidExceptionParser();

        AbstractThrowableParser<MethodArgumentNotValidException> firstInstance = parser.instantiate();
        AbstractThrowableParser<MethodArgumentNotValidException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        MethodArgumentNotValidExceptionParser parser = new MethodArgumentNotValidExceptionParser();

        MethodArgumentNotValidException mockException = mock(MethodArgumentNotValidException.class);

        BindingResult mockBindingResult = mock(BindingResult.class);

        List<FieldError> fieldErrorList = Arrays.asList(
                new FieldError("someObject1", "field1", "message 1"),
                new FieldError("someObject2", "field2", "message 2")
        );

        doReturn(fieldErrorList)
                .when(mockBindingResult)
                .getFieldErrors();

        doReturn(mockBindingResult)
                .when(mockException)
                .getBindingResult();

        ReflectionTestUtils.setField(parser, "throwable", mockException);

        String exceptionMessage = "message 1,message 2";
        String actual = parser.responseMessage();

        assertEquals(exceptionMessage, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        MethodArgumentNotValidExceptionParser parser = new MethodArgumentNotValidExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }
}
