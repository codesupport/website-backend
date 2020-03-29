package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.common.util.ValidationUtils;
import dev.codesupport.web.domain.validation.annotation.PasswordConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validation logic to be performed on properties annotated with {@link PasswordConstraint}
 */
public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    /**
     * Validates the value of the passwordField.
     *
     * @param passwordField The value of the property
     * @param cxt           Reference to the validation context
     * @return True if the value is valid, False otherwise.
     */
    @Override
    public boolean isValid(String passwordField,
                           ConstraintValidatorContext cxt) {
        return ValidationUtils.isValidPassword(passwordField);
    }

}
