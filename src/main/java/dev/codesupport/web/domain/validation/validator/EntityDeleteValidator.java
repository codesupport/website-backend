package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator;
import dev.codesupport.web.domain.validation.annotation.EntityDeleteConstraint;

public class EntityDeleteValidator implements MultiViolationConstraintValidator<EntityDeleteConstraint, IdentifiableDomain<Long>> {

    @Override
    public void validate(IdentifiableDomain<Long> domainObject, Violation violation) {
        if (domainObject.getId() == null) {
            violation.nullValue("id");
        }
    }

}
