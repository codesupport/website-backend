package dev.codesupport.web.common.domain;

import dev.codesupport.testutils.domain.MockValidatable;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AbstractValidatableTest {

    @Test
    public void shouldReturnCorectValidationIssueForMissingParameters() {
        AbstractValidatable<Long> abstractValidatable = new MockValidatable();

        String id = "1";
        String paramName = "param";

        ValidationIssue expected = new ValidationIssue(id, ValidationIssue.ValidationType.MISSING, paramName, "Missing parameter");
        ValidationIssue actual = abstractValidatable.createMissingParameter(id, paramName);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorectValidationIssueForInvalidParameters() {
        AbstractValidatable<Long> abstractValidatable = new MockValidatable();

        String id = "1";
        String paramName = "param";
        String hint = "hint";

        ValidationIssue expected = new ValidationIssue(id, ValidationIssue.ValidationType.INVALID, paramName, "Invalid parameter (" + hint + ")");
        ValidationIssue actual = abstractValidatable.createInvalidParameter(id, paramName, hint);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorectValidationIssueForDuplicateModel() {
        AbstractValidatable<Long> abstractValidatable = new MockValidatable();

        String id = "1";
        String paramName = "param";

        ValidationIssue expected = new ValidationIssue(id, ValidationIssue.ValidationType.DUPLICATE, paramName, "Must be unique in database");
        ValidationIssue actual = abstractValidatable.createDuplicateParameter(id, paramName);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorectValidationIssueForValidationIssue() {
        AbstractValidatable<Long> abstractValidatable = new MockValidatable();

        String id = "1";
        String paramName = "param";
        String message = "some validation message";

        ValidationIssue expected = new ValidationIssue(id, ValidationIssue.ValidationType.INVALID, paramName, message);
        ValidationIssue actual = abstractValidatable.createValidationIssue(id, ValidationIssue.ValidationType.INVALID, paramName, message);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldHaveAValidateMethod() {
        AbstractValidatable<Long> abstractValidatable = new MockValidatable();

        List<ValidationIssue> actual = abstractValidatable.validate();

        assertEquals(Collections.emptyList(), actual);
    }
}
