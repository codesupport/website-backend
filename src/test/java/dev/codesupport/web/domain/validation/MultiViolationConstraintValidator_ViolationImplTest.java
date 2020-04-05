package dev.codesupport.web.domain.validation;

import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator.ViolationImpl;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintValidatorContext;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MultiViolationConstraintValidator_ViolationImplTest {

    @Test
    public void shouldSetValidToDefaultTrue() {
        ConstraintValidatorContext mockContext = mock(ConstraintValidatorContext.class);

        ViolationImpl violationSpy = spy(new ViolationImpl(mockContext));

        assertTrue(
                (boolean) ReflectionTestUtils.getField(violationSpy, "valid")
        );
    }

    @Test
    public void shouldInvokeViolationFromMissing() {
        String propertyName = "parameter";

        ConstraintValidatorContext mockContext = mock(ConstraintValidatorContext.class);

        ViolationImpl violationSpy = spy(new ViolationImpl(mockContext));

        doNothing()
                .when(violationSpy)
                .violation(any(), any());

        violationSpy.missing(propertyName);

        verify(violationSpy, times(1))
                .violation(propertyName, "can not be null/empty");
    }

    @Test
    public void shouldInvokeViolationFromInvalid() {
        String propertyName = "parameter";

        ConstraintValidatorContext mockContext = mock(ConstraintValidatorContext.class);

        ViolationImpl violationSpy = spy(new ViolationImpl(mockContext));

        doNothing()
                .when(violationSpy)
                .violation(any(), any());

        violationSpy.invalid(propertyName);

        verify(violationSpy, times(1))
                .violation(propertyName, "has an invalid value");
    }

    @Test
    public void shouldInvokeViolationFromInvalidValue() {
        String propertyName = "parameter";
        String value = "value";

        ConstraintValidatorContext mockContext = mock(ConstraintValidatorContext.class);

        ViolationImpl violationSpy = spy(new ViolationImpl(mockContext));

        doNothing()
                .when(violationSpy)
                .violation(any(), any());

        violationSpy.invalidValue(propertyName, value);

        verify(violationSpy, times(1))
                .violation(propertyName, "value '" + value + "' is invalid");
    }

    @Test
    public void shouldInvokeViolationFromNullValue() {
        String propertyName = "parameter";

        ConstraintValidatorContext mockContext = mock(ConstraintValidatorContext.class);

        ViolationImpl violationSpy = spy(new ViolationImpl(mockContext));

        doNothing()
                .when(violationSpy)
                .violation(any(), any());

        violationSpy.nullValue(propertyName);

        verify(violationSpy, times(1))
                .violation(propertyName, "can not be null");
    }

    @Test
    public void shouldSetValidToFalseOnViolation() {
        String propertyName = "parameter";
        String message = "message";

        ConstraintValidatorContext mockContext = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder mockBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext mockBuilderContext = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);

        doReturn(null)
                .when(mockBuilderContext)
                .addConstraintViolation();

        doReturn(mockBuilderContext)
                .when(mockBuilder)
                .addPropertyNode(propertyName);

        doReturn(mockBuilder)
                .when(mockContext)
                .buildConstraintViolationWithTemplate(message);

        ViolationImpl violationSpy = spy(new ViolationImpl(mockContext));

        ReflectionTestUtils.setField(violationSpy, "valid", true);

        violationSpy.violation(propertyName, message);

        assertFalse(
                (boolean) ReflectionTestUtils.getField(violationSpy, "valid")
        );
    }

    @Test
    public void shouldCorrectlyAddViolationToConstraintContext() {
        String propertyName = "parameter";
        String message = "message";

        ConstraintValidatorContext mockContext = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder mockBuilder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext mockBuilderContext = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);

        doReturn(null)
                .when(mockBuilderContext)
                .addConstraintViolation();

        doReturn(mockBuilderContext)
                .when(mockBuilder)
                .addPropertyNode(propertyName);

        doReturn(mockBuilder)
                .when(mockContext)
                .buildConstraintViolationWithTemplate(message);

        ViolationImpl violationSpy = spy(new ViolationImpl(mockContext));

        ReflectionTestUtils.setField(violationSpy, "context", mockContext);

        violationSpy.violation(propertyName, message);

        verify(mockBuilderContext, times(1))
                .addConstraintViolation();
    }

}
