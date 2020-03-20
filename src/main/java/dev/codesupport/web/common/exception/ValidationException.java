package dev.codesupport.web.common.exception;

import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import dev.codesupport.web.common.service.service.CrudOperations;

import lombok.Getter;

import java.util.List;

/**
 * Used to indicate domain level or persistence level validation issues.
 *
 * @see CrudOperations
 */
public class ValidationException extends ServiceLayerException {

    @Getter
    private final List<ValidationIssue> validationIssues;

    public ValidationException(List<ValidationIssue> validationIssues) {
        super("There were validation errors");
        this.validationIssues = validationIssues;
    }
}
