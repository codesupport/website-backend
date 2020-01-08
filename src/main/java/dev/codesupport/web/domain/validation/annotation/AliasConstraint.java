package dev.codesupport.web.domain.validation.annotation;

import dev.codesupport.web.domain.validation.constraint.AliasValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AliasValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AliasConstraint {
    String message() default "Invalid alias: must be 4 - 15 alphanumeric characters, beginning with a letter";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}