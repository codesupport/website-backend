package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.common.util.ListUtils;
import dev.codesupport.web.common.util.StringUtils;
import dev.codesupport.web.domain.Contributor;
import dev.codesupport.web.domain.ContributorList;
import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator;
import dev.codesupport.web.domain.validation.Violation;
import dev.codesupport.web.domain.validation.annotation.ContributorListConstraint;

import java.util.List;

/**
 * Validation logic to be performed on properties annotated with {@link ContributorListConstraint}
 */
public class ContributorListValidator implements MultiViolationConstraintValidator<ContributorListConstraint, ContributorList> {

    /**
     * Validates a {@link ContributorList} object
     *
     * @param contributorList The {@link ContributorList} object to validate
     * @param violation       Reference to the violation helper object used for this validation
     */
    @Override
    public void validate(ContributorList contributorList, Violation violation) {
        validateContributorList(contributorList, violation, null);
    }

    /**
     * Validates a {@link ContributorList} object
     * <p>Accepts a prefix when dealing with nested objects for user clarity</p>
     *
     * @param contributorList The {@link ContributorList} object to validate
     * @param violation       Reference to the violation helper object used for this validation
     * @param prefix          The string to prefix parameters with for readability
     */
    public void validateContributorList(ContributorList contributorList, Violation violation, String prefix) {
        List<Contributor> contributors = contributorList.getContributors();

        if (ListUtils.isEmpty(contributors)) {
            violation.violation(
                    StringUtils.joinNonNull(".", prefix, ContributorList.Fields.contributors),
                    "must contain at least 1 contributor"
            );
        } else {
            // If contributors contains at least 1 contributor, validate it(them)
            ContributorValidator contributorValidator = contributorValidator();

            for (int i = 0; i < contributors.size(); i++) {
                contributorValidator.validateContributor(
                        contributors.get(i),
                        violation,
                        StringUtils.joinNonNull(".", prefix, ContributorList.Fields.contributors + "[" + i + "]")
                );
            }
        }
    }

    /**
     * Builds a {@link ContributorValidator} instance
     * <p>Exists simply for easier unit testing</p>
     *
     * @return A new {@link ContributorValidator} instance
     */
    ContributorValidator contributorValidator() {
        return new ContributorValidator();
    }
}
