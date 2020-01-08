package dev.codesupport.web.domain.validation.constraint;

import dev.codesupport.web.common.util.ValidationUtils;
import dev.codesupport.web.domain.validation.annotation.PasswordConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    @Override
    public void initialize(PasswordConstraint password) {
        // Empty because there's nothing to go here.
    }

    @Override
    public boolean isValid(String passwordField,
                           ConstraintValidatorContext cxt) {
        return ValidationUtils.isValidPassword(passwordField);
    }

}
