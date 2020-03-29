package dev.codesupport.web.domain.validation.constraint;

import dev.codesupport.web.domain.validation.annotation.PasswordConstraint;
import dev.codesupport.web.domain.validation.validator.PasswordValidator;
import org.assertj.core.util.Lists;
import org.junit.Test;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PasswordConstraintTest {

    @Test
    public void shouldBeAnnotatedWithCorrectValidator() {
        Constraint annotation = PasswordConstraint.class.getAnnotation(Constraint.class);

        List<Class<?>> expected = Collections.singletonList(PasswordValidator.class);
        List<Class<?>> actual = Lists.newArrayList(
                annotation.validatedBy()
        );

        assertEquals(expected, actual);
    }

    @Test
    public void shouldBeAnnotatedWithCorrectRetentionPolicy() {
        Retention annotation = PasswordConstraint.class.getAnnotation(Retention.class);

        RetentionPolicy expected = RetentionPolicy.RUNTIME;
        RetentionPolicy actual = annotation.value();

        assertEquals(expected, actual);
    }

}
