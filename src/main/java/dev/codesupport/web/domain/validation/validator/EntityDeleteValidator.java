package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator;
import dev.codesupport.web.domain.validation.Violation;
import dev.codesupport.web.domain.validation.annotation.EntityDeleteConstraint;

/**
 * Validation logic to be performed on properties annotated with {@link EntityDeleteConstraint}
 */
public class EntityDeleteValidator implements MultiViolationConstraintValidator<EntityDeleteConstraint, IdentifiableDomain<Long>> {

    /**
     * Validates a {@link IdentifiableDomain} object
     * <p>Used to verify that the passed objects contains an ID that can be used to reference in the delete process</p>
     *
     * @param domainObject The {@link IdentifiableDomain} object to validate
     * @param violation    Reference to the violation helper object used for this validation
     */
    @Override
    public void validate(IdentifiableDomain<Long> domainObject, Violation violation) {
        if (domainObject.getId() == null) {
            violation.nullValue("id");
        }
    }

}
