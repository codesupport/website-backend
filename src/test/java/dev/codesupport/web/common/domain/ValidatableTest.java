package dev.codesupport.web.common.domain;

import dev.codesupport.testutils.domain.MockValidatable;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ValidatableTest {

    @Test
    public void shouldReturnCorectValidationIssueForMissingParameters() {
        Validatable<Long> validatable = new MockValidatable();

        String id = "1";
        String paramName = "param";

        ValidationIssue expected = new ValidationIssue(id, ValidationIssue.ValidationType.MISSING, paramName, "Missing parameter");
        ValidationIssue actual = validatable.createMissingParameter(id, paramName);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorectValidationIssueForInvalidParameters() {
        Validatable<Long> validatable = new MockValidatable();

        String id = "1";
        String paramName = "param";
        String hint = "hint";

        ValidationIssue expected = new ValidationIssue(id, ValidationIssue.ValidationType.INVALID, paramName, "Invalid parameter (" + hint + ")");
        ValidationIssue actual = validatable.createInvalidParameter(id, paramName, hint);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorectValidationIssueForDuplicateModel() {
        Validatable<Long> validatable = new MockValidatable();

        String id = "1";
        String paramName = "param";

        ValidationIssue expected = new ValidationIssue(id, ValidationIssue.ValidationType.DUPLICATE, paramName, "Must be unique in database");
        ValidationIssue actual = validatable.createDuplicateParameter(id, paramName);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorectValidationIssueForValidationIssue() {
        Validatable<Long> validatable = new MockValidatable();

        String id = "1";
        String paramName = "param";
        String message = "some validation message";

        ValidationIssue expected = new ValidationIssue(id, ValidationIssue.ValidationType.INVALID, paramName, message);
        ValidationIssue actual = validatable.createValidationIssue(id, ValidationIssue.ValidationType.INVALID, paramName, message);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldHaveAValidateMethod() {
        Validatable<Long> validatable = new MockValidatable();

        List<ValidationIssue> actual = validatable.validate();

        assertEquals(Collections.emptyList(), actual);
    }
}
