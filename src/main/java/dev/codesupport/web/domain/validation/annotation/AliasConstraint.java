package dev.codesupport.web.domain.validation.annotation;

import dev.codesupport.web.domain.validation.validator.AliasValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate properties that must be valid Aliases
 */
@Documented
@Constraint(validatedBy = AliasValidator.class)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AliasConstraint {
    /**
     * @return Message to display if property is invalid
     */
    String message() default "Invalid alias: must be 4 - 15 alphanumeric characters, beginning with a letter";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}