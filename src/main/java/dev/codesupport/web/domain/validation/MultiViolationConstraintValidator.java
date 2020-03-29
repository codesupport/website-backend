package dev.codesupport.web.domain.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * Interface for abstracting away the creation of custom multi violation messages.
 *
 * @param <C> The {@link javax.validation.Constraint} annotation for the specific validation
 * @param <T> The domain object associated with the constraint validation
 */
public interface MultiViolationConstraintValidator<C extends Annotation, T> extends ConstraintValidator<C, T> {

    /**
     * Creates new {@link Violation} instance object for new validation process.
     * <p>Disables default constraint validation behavior</p>
     *
     * @param object                     The domain object being validated
     * @param constraintValidatorContext The constraint context used to track validation issues
     * @return True if no issues were reported, False otherwise.
     */
    @Override
    default boolean isValid(T object, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        Violation violation = new ViolationImpl(constraintValidatorContext);

        validate(object, violation);

        return violation.isValid();
    }

    /**
     * Sub class declared method used to define the logic of the validation.
     * <p>Logic should utilize the {@link Violation} object instance to report validation issues.</p>
     *
     * @param object    The domain object to be validated
     * @param violation The {@link Violation} instance object used for reporting issues
     */
    void validate(T object, Violation violation);

    /**
     * Implementation of the {@link Violation} interface.
     * <p>Utilizes the {@link ConstraintValidatorContext} to report issues with the various provided {@link Violation} methods</p>
     */
    @RequiredArgsConstructor
    class ViolationImpl implements Violation {

        // This is passed in via constructor
        private final ConstraintValidatorContext context;

        @Getter
        // Tracks the validity of the object being validated
        private boolean valid = true;

        /**
         * Reports a property that is either null or otherwise empty
         *
         * @param propertyName The name of the property reported
         */
        public void missing(String propertyName) {
            violation(propertyName, "can not be null/empty");
        }

        /**
         * Reports a property that is invalid
         *
         * @param propertyName The name of the property reported
         */
        public void invalid(String propertyName) {
            violation(propertyName, "has an invalid value");
        }

        /**
         * Reports a property that is invalid
         *
         * @param propertyName The name of the property reported
         * @param value        The invalid value that was assigned
         */
        public void invalidValue(String propertyName, String value) {
            violation(propertyName, "value '" + value + "' is invalid");
        }

        /**
         * Reports a property that is null
         *
         * @param propertyName The name of the property reported
         */
        public void nullValue(String propertyName) {
            violation(propertyName, "can not be null");
        }

        /**
         * Reports a property with a given message
         * <p>Flags the object as invalid when any violation is reported</p>
         *
         * @param propertyName The name of the property reported
         * @param message      The message describing the property's validation issue
         */
        public void violation(String propertyName, String message) {
            valid = false;
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(propertyName)
                    .addConstraintViolation();
        }

    }
}
