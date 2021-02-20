package dev.codesupport.web.domain.validation.annotation;

import dev.codesupport.web.domain.validation.validator.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate properties that must be valid emails
 */
@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailConstraint {
    /**
     * @return Message to display if property is invalid
     */
    String message() default "Your email address is not in a valid format.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}