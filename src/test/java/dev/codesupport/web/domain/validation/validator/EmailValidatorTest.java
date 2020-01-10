package dev.codesupport.web.domain.validation.validator;

import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class EmailValidatorTest {

    @Test
    public void shouldReturnFalseIfEmailIsInvalid() {
        EmailValidator validator = new EmailValidator();

        String field = "email";
        ConstraintValidatorContext mockValidatorContext = mock(ConstraintValidatorContext.class);

        assertFalse(validator.isValid(field, mockValidatorContext));
    }

    @Test
    public void shouldReturnFalseIfEmailIsValid() {
        EmailValidator validator = new EmailValidator();

        String field = "email@email.em";
        ConstraintValidatorContext mockValidatorContext = mock(ConstraintValidatorContext.class);

        assertTrue(validator.isValid(field, mockValidatorContext));
    }

}
