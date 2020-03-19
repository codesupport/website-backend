package dev.codesupport.web.common.service.data.validation;

import dev.codesupport.web.common.service.validation.persistant.AbstractPersistenceValidator;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Used to define a specific validation issue for domain level and persistence level validations
 *
 * @see AbstractPersistenceValidator
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

    /**
     * Creates a new {@link ValidationIssue} for a missing property.
     *
     * @param id           The id of the resource with the missing property.
     * @param propertyName The name of the property that is missing.
     * @return A new {@link ValidationIssue} object.
     */
    public static ValidationIssue missingParameter(String id, String propertyName) {
        return new ValidationIssue(id, ValidationIssue.ValidationType.MISSING, propertyName, "Missing parameter");
    }

    /**
     * Creates a new {@link ValidationIssue} for a invalid property.
     *
     * @param id           The id of the resource with the invalid property.
     * @param propertyName The name of the property that is invalid.
     * @return A new {@link ValidationIssue} object.
     */
    public static ValidationIssue invalidParameter(String id, String propertyName, String hint) {
        return new ValidationIssue(id, ValidationIssue.ValidationType.INVALID, propertyName, "Invalid parameter (" + hint + ")");
    }

    /**
     * Creates a new {@link ValidationIssue} for a duplicate property value.
     *
     * @param id           The id of the resource with the duplicate property value.
     * @param propertyName The name of the property that has a duplicate value.
     * @return A new {@link ValidationIssue} object.
     */
    public static ValidationIssue duplicateParameter(String id, String propertyName) {
        return new ValidationIssue(id, ValidationIssue.ValidationType.DUPLICATE, propertyName, "Must be unique in database");
    }

}
