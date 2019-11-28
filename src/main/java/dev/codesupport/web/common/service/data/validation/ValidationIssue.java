package dev.codesupport.web.common.service.data.validation;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Used to define a specific validation issue for domain level and persistence level validations
 * @see dev.codesupport.web.common.domain.Validatable
 * @see dev.codesupport.web.common.service.validation.persistant.AbstractPersistenceValidation
 */
@Data
@AllArgsConstructor
public class ValidationIssue {

    /**
     * The type of validation issue related to this issue
     */
    public enum ValidationType {
        /**
         * Represents a missing element found in the validation process.
         */
        MISSING,
        /**
         * Represents an invalid element found in the validation process.
         */
        INVALID,
        /**
         * Represents a duplicate element found in the validation process.
         */
        DUPLICATE
    }

    /**
     * Id of the related resource causing the validation issue, if there is one.
     */
    private String id;
    /**
     * The {@link ValidationType} associated to the issue.
     */
    private ValidationType type;
    /**
     * The name of the property associated with the validation issue, if there is one.
     */
    private String propertyName;
    /**
     * Information regarding the reason for the validation issue.
     */
    private String message;

}
