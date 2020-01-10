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
     * No logic implemented for this validation
     *
     * @param password Reference to the constraint annotation
     */
    @Override
    public void initialize(PasswordConstraint password) {
        // Empty because there's nothing to go here.
    }

    /**
     * Validates the value of the passwordField.
     *
     * @param passwordField The value of the property
     * @param cxt           ?
     * @return True if the value is valid, False otherwise.
     */
    @Override
    public boolean isValid(String passwordField,
                           ConstraintValidatorContext cxt) {
        return ValidationUtils.isValidPassword(passwordField);
    }

}
