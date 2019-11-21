package dev.codesupport.web.common.service.data.validation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationIssue {

    public enum ValidationType {
        MISSING,
        INVALID,
        DUPLICATE
    }

    private String id;
    private ValidationType type;
    private String parameterName;
    private String message;

}
