package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.domain.Contributor;
import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator;
import dev.codesupport.web.domain.validation.annotation.ContributorConstraint;

public class ContributorValidator implements MultiViolationConstraintValidator<ContributorConstraint, Contributor> {

    @Override
    public void validate(Contributor contributor, Violation violation) {
        if (contributor.getAlias() == null && (contributor.getUser() == null || contributor.getUser().getId() == null)) {
            violation.violation(Contributor.Fields.alias, "must include alias or user id");
        }
    }

}
