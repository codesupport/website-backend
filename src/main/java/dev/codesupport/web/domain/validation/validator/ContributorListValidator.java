package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.common.util.ListUtils;
import dev.codesupport.web.domain.Contributor;
import dev.codesupport.web.domain.ContributorList;
import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator;
import dev.codesupport.web.domain.validation.annotation.ContributorListConstraint;

import java.util.List;

public class ContributorListValidator implements MultiViolationConstraintValidator<ContributorListConstraint, ContributorList> {

    @Override
    public void validate(ContributorList contributorList, Violation violation) {
        List<Contributor> contributors = contributorList.getContributors();

        if (ListUtils.isEmpty(contributors)) {
            violation.violation(ContributorList.Fields.contributors, "must contain at least 1 contributor");
        } else {
            ContributorValidator contributorValidator = new ContributorValidator();
            contributors.forEach(contributor -> contributorValidator.validate(contributor, violation));
        }
    }

}
