package dev.codesupport.testutils.validation;

import dev.codesupport.web.domain.validation.Violation;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TestViolation implements Violation {

    private boolean valid;

    private Set<String> violations = new HashSet<>();

    @Override
    public void missing(String propertyName) {
        violation(propertyName, "missing");
    }

    @Override
    public void invalid(String propertyName) {
        violation(propertyName, "invalid");
    }

    @Override
    public void invalidValue(String propertyName, String value) {
        violation(propertyName, value + " invalidValue");
    }

    @Override
    public void nullValue(String propertyName) {
        violation(propertyName, "nullValue");
    }

    @Override
    public void violation(String propertyName, String message) {
        violations.add(propertyName + " " + message);
    }
}
