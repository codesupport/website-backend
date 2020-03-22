package dev.codesupport.web.domain.validation;

import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public interface MultiViolationConstraintValidator<C extends Annotation, T> extends ConstraintValidator<C, T> {

    @Override
    default boolean isValid(T object, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        Violation violation = new Violation(constraintValidatorContext);

        validate(object, violation);

        return violation.hasNoViolations;
    }

    void validate(T object, Violation violation);

    @RequiredArgsConstructor
    class Violation {

        private final ConstraintValidatorContext context;
        private boolean hasNoViolations = true;

        public void missing(String propertyName) {
            violation(propertyName, "can not be null/empty");
        }

        public void invalid(String propertyName) {
            violation(propertyName, "has an invalid value");
        }

        public void invalidValue(String propertyName, String value) {
            violation(propertyName, "value '" + value + "' is invalid");
        }

        public void nullValue(String propertyName) {
            violation(propertyName, "can not be null");
        }

        public void violation(String propertyName, String message) {
            hasNoViolations = false;
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(propertyName)
                    .addConstraintViolation();
        }

    }
}
