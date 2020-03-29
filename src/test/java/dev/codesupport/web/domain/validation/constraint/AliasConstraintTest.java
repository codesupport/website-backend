package dev.codesupport.web.domain.validation.constraint;

import dev.codesupport.web.domain.validation.annotation.AliasConstraint;
import dev.codesupport.web.domain.validation.validator.AliasValidator;
import org.assertj.core.util.Lists;
import org.junit.Test;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AliasConstraintTest {

    @Test
    public void shouldBeAnnotatedWithCorrectValidator() {
        Constraint annotation = AliasConstraint.class.getAnnotation(Constraint.class);

        List<Class<?>> expected = Collections.singletonList(AliasValidator.class);
        List<Class<?>> actual = Lists.newArrayList(
                annotation.validatedBy()
        );

        assertEquals(expected, actual);
    }

    @Test
    public void shouldBeAnnotatedWithCorrectRetentionPolicy() {
        Retention annotation = AliasConstraint.class.getAnnotation(Retention.class);

        RetentionPolicy expected = RetentionPolicy.RUNTIME;
        RetentionPolicy actual = annotation.value();

        assertEquals(expected, actual);
    }

}
