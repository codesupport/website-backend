package dev.codesupport.web.common.exception;

import lombok.Getter;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;

import java.util.List;

public class ValidationException extends ServiceLayerException {

    @Getter
    private final List<ValidationIssue> validationIssues;

    public ValidationException(List<ValidationIssue> validationIssues) {
        super("There were validation errors");
        this.validationIssues = validationIssues;
    }
}
