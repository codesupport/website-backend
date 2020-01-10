package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.common.util.ValidationUtils;
import dev.codesupport.web.domain.validation.annotation.EmailConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validation logic to be performed on properties annotated with {@link EmailConstraint}
 */
public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {

    /**
     * No logic implemented for this validation
     *
     * @param email Reference to the constraint annotation
     */
    @Override
    public void initialize(EmailConstraint email) {
        // Empty because there's nothing to go here.
    }

    /**
     * Validates the value of the emailField.
     *
     * @param emailField The value of the email property
     * @param cxt        ?
     * @return True if the value is valid, False otherwise.
     */
    @Override
    public boolean isValid(String emailField,
                           ConstraintValidatorContext cxt) {
        return ValidationUtils.isValidEmail(emailField);
    }

}
