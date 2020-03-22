package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.domain.Showcase;
import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator;
import dev.codesupport.web.domain.validation.annotation.ShowcaseConstraint;
import org.apache.commons.lang3.StringUtils;

public class ShowcaseValidator implements MultiViolationConstraintValidator<ShowcaseConstraint, Showcase> {

    @Override
    public void validate(Showcase showcase, Violation violation) {
        if (StringUtils.isBlank(showcase.getTitle())) {
            violation.missing(Showcase.Fields.title);
        }

        if (StringUtils.isBlank(showcase.getDescription())) {
            violation.missing(Showcase.Fields.description);
        }

        if (StringUtils.isBlank(showcase.getLink())) {
            violation.missing(Showcase.Fields.link);
        }

        if (showcase.getContributorList() == null) {
            violation.missing(Showcase.Fields.contributorList);
        } else {
            ContributorListValidator contributorListValidator = new ContributorListValidator();

            contributorListValidator.validate(showcase.getContributorList(), violation);
        }
    }

}
