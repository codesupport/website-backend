package dev.codesupport.web.domain.validation.annotation;

import dev.codesupport.web.domain.validation.validator.ArticleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ArticleValidator.class)
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ArticleConstraint {
    String message() default "Invalid article";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}