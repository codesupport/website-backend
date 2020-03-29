package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.common.util.StringUtils;
import dev.codesupport.web.domain.Contributor;
import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator;
import dev.codesupport.web.domain.validation.Violation;
import dev.codesupport.web.domain.validation.annotation.ContributorConstraint;

/**
 * Validation logic to be performed on properties annotated with {@link ContributorConstraint}
 */
public class ContributorValidator implements MultiViolationConstraintValidator<ContributorConstraint, Contributor> {

    /**
     * Validates a {@link Contributor} object
     *
     * @param contributor The {@link Contributor} object to validate
     * @param violation   Reference to the violation helper object used for this validation
     */
    @Override
    public void validate(Contributor contributor, Violation violation) {
        validateContributor(contributor, violation, null);
    }

    /**
     * Validates a {@link Contributor} object
     * <p>Accepts a prefix when dealing with nested objects for user clarity</p>
     *
     * @param contributor The {@link Contributor} object to validate
     * @param violation   Reference to the violation helper object used for this validation
     * @param prefix      The string to prefix parameters with for readability
     */
    public void validateContributor(Contributor contributor, Violation violation, String prefix) {
        if (contributor.getAlias() == null && (contributor.getUser() == null || contributor.getUser().getId() == null)) {
            violation.violation(
                    StringUtils.joinNonNull(".", prefix, Contributor.Fields.alias),
                    "must include alias or user id"
            );
        }
    }

}
