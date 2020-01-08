package dev.codesupport.web.domain.validation.constraint;

import dev.codesupport.web.common.util.ValidationUtils;
import dev.codesupport.web.domain.validation.annotation.EmailConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {

    @Override
    public void initialize(EmailConstraint email) {
        // Empty because there's nothing to go here.
    }

    @Override
    public boolean isValid(String emailField,
                           ConstraintValidatorContext cxt) {
        return ValidationUtils.isValidEmail(emailField);
    }

}
