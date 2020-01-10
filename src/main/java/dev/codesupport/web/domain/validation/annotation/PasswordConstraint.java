package dev.codesupport.web.domain.validation.annotation;

import dev.codesupport.web.domain.validation.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate properties that must be valid passwords
 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
    /**
     * @return Message to display if property is invalid
     */
    String message() default "Invalid password: must be alphanumeric and length > 10";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}