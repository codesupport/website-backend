package dev.codesupport.web.domain.validation.annotation;

import dev.codesupport.web.domain.validation.validator.ContributorValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ContributorValidator.class)
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContributorConstraint {
    String message() default "Invalid contributor";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
