package dev.codesupport.web.domain.validation.validator;

import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AliasValidatorTest {

    @Test
    public void shouldReturnFalseIfAliasIsInvalid() {
        AliasValidator validator = new AliasValidator();

        String field = "1user";
        ConstraintValidatorContext mockValidatorContext = mock(ConstraintValidatorContext.class);

        assertFalse(validator.isValid(field, mockValidatorContext));
    }

    @Test
    public void shouldReturnFalseIfAliasIsValid() {
        AliasValidator validator = new AliasValidator();

        String field = "username";
        ConstraintValidatorContext mockValidatorContext = mock(ConstraintValidatorContext.class);

        assertTrue(validator.isValid(field, mockValidatorContext));
    }

}
