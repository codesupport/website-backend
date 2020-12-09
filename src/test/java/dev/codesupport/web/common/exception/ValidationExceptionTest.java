package dev.codesupport.web.common.exception;

import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ValidationExceptionTest {

    @Test
    public void shouldCorrectlySetExceptionMessage() {
        ValidationException exception = new ValidationException(Collections.emptyList());

        assertEquals("There were validation errors", exception.getMessage());
    }

    @Test
    public void shouldAlsoCorrectlySetExceptionMessage() {
        List<ValidationIssue> mockValidationIssues = Collections.singletonList(
                mock(ValidationIssue.class)
        );

        ValidationException exception = new ValidationException(mockValidationIssues);

        assertEquals(mockValidationIssues, exception.getValidationIssues());
    }

}
