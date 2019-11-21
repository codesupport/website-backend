package dev.codesupport.web.common.domain;

import dev.codesupport.web.common.service.data.validation.ValidationIssue;

import java.util.List;

public interface Validatable<I> extends IdentifiableDomain<I> {

    List<ValidationIssue> validate();

    default ValidationIssue createMissingParameter(String id, String parameterName) {
        return createValidationIssue(id, ValidationIssue.ValidationType.MISSING, parameterName, "Missing parameter");
    }

    default ValidationIssue createInvalidParameter(String id, String parameterName, String hint) {
        return createValidationIssue(id, ValidationIssue.ValidationType.INVALID, parameterName, "Invalid parameter (" + hint + ")");
    }

    default ValidationIssue createDuplicateParameter(String id, String parameterName) {
        return createValidationIssue(id, ValidationIssue.ValidationType.DUPLICATE, parameterName, "Must be unique in database");
    }

    default ValidationIssue createValidationIssue(String id, ValidationIssue.ValidationType type, String parameterName, String issueMessage) {
        return new ValidationIssue(id, type, parameterName, issueMessage);
    }

}
