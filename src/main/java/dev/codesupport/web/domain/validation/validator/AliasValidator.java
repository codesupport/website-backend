package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.common.util.ValidationUtils;
import dev.codesupport.web.domain.validation.annotation.AliasConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validation logic to be performed on properties annotated with {@link AliasConstraint}
 */
public class AliasValidator implements ConstraintValidator<AliasConstraint, String> {

    /**
     * No logic implemented for this validation
     *
     * @param alias Reference to the constraint annotation
     */
    @Override
    public void initialize(AliasConstraint alias) {
        // Empty because there's nothing to go here.
    }

    /**
     * Validates the value of the aliasField.
     *
     * @param aliasField The value of the alias property
     * @param cxt        ?
     * @return True if the value is valid, False otherwise.
     */
    @Override
    public boolean isValid(String aliasField,
                           ConstraintValidatorContext cxt) {
        return ValidationUtils.isValidAlias(aliasField);
    }

}
