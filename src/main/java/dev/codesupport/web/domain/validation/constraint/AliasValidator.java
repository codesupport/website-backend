package dev.codesupport.web.domain.validation.constraint;

import dev.codesupport.web.common.util.ValidationUtils;
import dev.codesupport.web.domain.validation.annotation.AliasConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AliasValidator implements ConstraintValidator<AliasConstraint, String> {

    @Override
    public void initialize(AliasConstraint alias) {
        // Empty because there's nothing to go here.
    }

    @Override
    public boolean isValid(String aliasField,
                           ConstraintValidatorContext cxt) {
        return ValidationUtils.isValidAlias(aliasField);
    }

}
