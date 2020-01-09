package dev.codesupport.web.domain.validation.validator;

import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class PasswordValidatorTest {

    @Test
    public void shouldReturnFalseIfPasswordIsInvalid() {
        PasswordValidator validator = new PasswordValidator();

        String field = "pass";
        ConstraintValidatorContext mockValidatorContext = mock(ConstraintValidatorContext.class);

        assertFalse(validator.isValid(field, mockValidatorContext));
    }

    @Test
    public void shouldReturnFalseIfPasswordIsValid() {
        PasswordValidator validator = new PasswordValidator();

        String field = "mylongandsecurepassword";
        ConstraintValidatorContext mockValidatorContext = mock(ConstraintValidatorContext.class);

        assertTrue(validator.isValid(field, mockValidatorContext));
    }

}
