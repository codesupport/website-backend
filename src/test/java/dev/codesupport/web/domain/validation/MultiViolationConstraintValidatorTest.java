package dev.codesupport.web.domain.validation;

import lombok.Getter;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MultiViolationConstraintValidatorTest {

    private static class TestValidator implements MultiViolationConstraintValidator<Constraint, Long> {

        @Getter
        private Violation violation;

        @Override
        public void validate(Long object, Violation violation) {
            this.violation = violation;
        }

    }

    @Test
    public void shouldInvokeDisableDefaultConstraintViolation() {
        TestValidator validator = new TestValidator();

        ConstraintValidatorContext mockConstraintValidatorContext = mock(ConstraintValidatorContext.class);

        doNothing()
                .when(mockConstraintValidatorContext)
                .disableDefaultConstraintViolation();

        validator.isValid(5L, mockConstraintValidatorContext);

        verify(mockConstraintValidatorContext, times(1))
                .disableDefaultConstraintViolation();
    }

    @Test
    public void shouldPassConstraintValidatorContextToViolationClass() {
        TestValidator validator = new TestValidator();

        ConstraintValidatorContext expected = mock(ConstraintValidatorContext.class);

        doNothing()
                .when(expected)
                .disableDefaultConstraintViolation();

        validator.isValid(5L, expected);

        Violation violation = validator.getViolation();

        ConstraintValidatorContext actual = (ConstraintValidatorContext) ReflectionTestUtils.getField(violation, "context");

        assertSame(expected, actual);
    }

}
