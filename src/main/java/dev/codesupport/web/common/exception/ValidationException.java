package dev.codesupport.web.common.exception;

import lombok.Getter;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;

import java.util.List;

/**
 * Used to indicate domain level or persistence level validation issues.
 *
 * @see dev.codesupport.web.common.domain.Validatable
 * @see dev.codesupport.web.common.service.service.CrudOperations
 */
public class ValidationException extends ServiceLayerException {

    @Getter
    private final List<ValidationIssue> validationIssues;

    public ValidationException(List<ValidationIssue> validationIssues) {
        super("There were validation errors");
        this.validationIssues = validationIssues;
    }
}
