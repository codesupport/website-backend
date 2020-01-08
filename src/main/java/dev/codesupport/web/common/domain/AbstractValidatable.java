package dev.codesupport.web.common.domain;

import dev.codesupport.web.common.service.data.validation.ValidationIssue;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;

/**
 * Implemented by domain classes to create domain level validations.
 *
 * @param <I> Associated type of the Id property for the domain class.
 */
@EqualsAndHashCode
// This goes against like even other language.
@SuppressWarnings("squid:S1610")
public abstract class AbstractValidatable<I> implements IdentifiableDomain<I> {

    public List<ValidationIssue> validate() {
        return Collections.emptyList();
    }

    /**
     * Creates a new {@link ValidationIssue} for a missing property.
     *
     * @param id           The id of the resource with the missing property.
     * @param propertyName The name of the property that is missing.
     * @return A new {@link ValidationIssue} object.
     */
    public final ValidationIssue createMissingParameter(String id, String propertyName) {
        return createValidationIssue(id, ValidationIssue.ValidationType.MISSING, propertyName, "Missing parameter");
    }

    /**
     * Creates a new {@link ValidationIssue} for a invalid property.
     *
     * @param id           The id of the resource with the invalid property.
     * @param propertyName The name of the property that is invalid.
     * @return A new {@link ValidationIssue} object.
     */
    public final ValidationIssue createInvalidParameter(String id, String propertyName, String hint) {
        return createValidationIssue(id, ValidationIssue.ValidationType.INVALID, propertyName, "Invalid parameter (" + hint + ")");
    }

    /**
     * Creates a new {@link ValidationIssue} for a duplicate property value.
     *
     * @param id           The id of the resource with the duplicate property value.
     * @param propertyName The name of the property that has a duplicate value.
     * @return A new {@link ValidationIssue} object.
     */
    public final ValidationIssue createDuplicateParameter(String id, String propertyName) {
        return createValidationIssue(id, ValidationIssue.ValidationType.DUPLICATE, propertyName, "Must be unique in database");
    }

    /**
     * @param id           The id of the resource with the associated property
     * @param type         The {@link dev.codesupport.web.common.service.data.validation.ValidationIssue.ValidationType} associated to the {@link ValidationIssue}.
     * @param propertyName The name of the property that has the associated issue.
     * @param issueMessage The message describing the associated issue.
     * @return A new {@link ValidationIssue} object.
     */
    final ValidationIssue createValidationIssue(String id, ValidationIssue.ValidationType type, String propertyName, String issueMessage) {
        return new ValidationIssue(id, type, propertyName, issueMessage);
    }

}
