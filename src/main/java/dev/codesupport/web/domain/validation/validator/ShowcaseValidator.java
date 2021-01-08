package dev.codesupport.web.domain.validation.validator;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.domain.Showcase;
import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator;
import dev.codesupport.web.domain.validation.Violation;
import dev.codesupport.web.domain.validation.annotation.ShowcaseConstraint;
import org.apache.commons.lang3.StringUtils;

/**
 * Validation logic to be performed on properties annotated with {@link ShowcaseConstraint}
 */
public class ShowcaseValidator implements MultiViolationConstraintValidator<ShowcaseConstraint, Showcase> {

    private boolean idRequired;

    @Override
    public void initialize(ShowcaseConstraint constraintAnnotation) {
        idRequired = constraintAnnotation.requireId();
    }

    /**
     * Validates a {@link Showcase} object
     *
     * @param showcase  The {@link Showcase} object to validate
     * @param violation Reference to the violation helper object used for this validation
     */
    @Override
    public void validate(Showcase showcase, Violation violation) {
        if (idRequired) {
            if (showcase.getId() == null) {
                violation.nullValue(Showcase.Fields.id);
            } else if (showcase.getId() == 0){
                violation.invalid(Showcase.Fields.id);
            }
        }

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
            violation.nullValue(Showcase.Fields.contributorList);
        } else {
            // If ContributorList exists, validate it
            ContributorListValidator contributorListValidator = contributorListValidator();

            contributorListValidator.validateContributorList(showcase.getContributorList(), violation, Showcase.Fields.contributorList);
        }
    }

    /**
     * Builds a {@link ContributorListValidator} instance
     * <p>Exists simply for easier unit testing</p>
     *
     * @return A new {@link ContributorListValidator} instance
     */
    @VisibleForTesting
    ContributorListValidator contributorListValidator() {
        return new ContributorListValidator();
    }

}
