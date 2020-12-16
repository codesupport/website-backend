package dev.codesupport.web.domain.validation.annotation;

import dev.codesupport.web.domain.validation.validator.ImageUploadValidator;

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
@Constraint(validatedBy = ImageUploadValidator.class)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageUploadConstraint {

    /**
     * @return Message to display if property is invalid
     */
    String message() default "Invalid filetype, only accept png, jpg, svg";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] types() default {"image/png", "image/jpg", "image/jpeg", "image/svg+xml"};

}