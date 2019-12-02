package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.ValidationException;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.service.service.RestStatus;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ValidationExceptionParserTest {

    @Test
    public void shouldReturnCorrectParserType() {
        ValidationExceptionParser parser = new ValidationExceptionParser();

        AbstractThrowableParser<ValidationException> instancedParser = parser.instantiate();

        assertTrue(instancedParser instanceof ValidationExceptionParser);
    }

    @Test
    public void shouldCreateNewInstance() {
        ValidationExceptionParser parser = new ValidationExceptionParser();

        AbstractThrowableParser<ValidationException> firstInstance = parser.instantiate();
        AbstractThrowableParser<ValidationException> secondInstance = parser.instantiate();

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldReturnCorrectMessage() {
        String parameterName = "ParameterName";
        String validationMessage = "Validation Message";

        ValidationExceptionParser parser = new ValidationExceptionParser();

        ValidationIssue mockValidationIssue = mock(ValidationIssue.class);

        //ResultOfMethodCallIgnored - We're not calling a method, we're making a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(parameterName)
                .when(mockValidationIssue)
                .getPropertyName();

        //ResultOfMethodCallIgnored - We're not calling a method, we're making a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(validationMessage)
                .when(mockValidationIssue)
                .getMessage();

        List<ValidationIssue> validationIssues = Collections.singletonList(
                mockValidationIssue
        );

        ValidationException mockException = mock(ValidationException.class);

        //ResultOfMethodCallIgnored - We're not calling a method, we're making a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(validationIssues)
                .when(mockException)
                .getValidationIssues();

        ReflectionTestUtils.setField(parser, "throwable", mockException);

        String expected = "[" + parameterName + ": " + validationMessage + "]";
        String actual = parser.responseMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectStatus() {
        ValidationExceptionParser parser = new ValidationExceptionParser();

        RestStatus expected = RestStatus.FAIL;
        RestStatus actual = ReflectionTestUtils.invokeMethod(parser, "responseStatus");

        assertEquals(expected, actual);
    }
}
