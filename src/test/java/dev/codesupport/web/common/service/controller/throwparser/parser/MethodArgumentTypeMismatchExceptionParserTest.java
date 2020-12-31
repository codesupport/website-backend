package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class MethodArgumentTypeMismatchExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        MethodArgumentTypeMismatchExceptionParser parser = new MethodArgumentTypeMismatchExceptionParser();

        AbstractThrowableParser<MethodArgumentTypeMismatchException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof MethodArgumentTypeMismatchExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        MethodArgumentTypeMismatchExceptionParser parser = new MethodArgumentTypeMismatchExceptionParser();

        AbstractThrowableParser<MethodArgumentTypeMismatchException> firstInstance = parser.instantiate();
        AbstractThrowableParser<MethodArgumentTypeMismatchException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        String parameterName = "ParameterName";
        Class<Long> parameterType = Long.class;

        MethodArgumentTypeMismatchExceptionParser parser = new MethodArgumentTypeMismatchExceptionParser();

        MethodArgumentTypeMismatchException mockException = mock(MethodArgumentTypeMismatchException.class);

        MethodParameter mockMethodParameter = mock(MethodParameter.class);

        doReturn(mockMethodParameter)
                .when(mockException)
                .getParameter();

        doReturn(parameterType)
                .when(mockMethodParameter)
                .getParameterType();

        doReturn(parameterName)
                .when(mockException)
                .getName();

        ReflectionTestUtils.setField(parser, "throwable", mockException);

        List<String> expected = Collections.singletonList(
                "Invalid parameter " + parameterName + " (Required: " + parameterType.getSimpleName() + ")."
        );
        List<String> actual = parser.responseMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        MethodArgumentTypeMismatchExceptionParser parser = new MethodArgumentTypeMismatchExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatusCode() {
        MethodArgumentTypeMismatchExceptionParser parser = new MethodArgumentTypeMismatchExceptionParser();

        int expected = 400;
        int actual = parser.responseCode();

        assertEquals(expected, actual);
    }

}
