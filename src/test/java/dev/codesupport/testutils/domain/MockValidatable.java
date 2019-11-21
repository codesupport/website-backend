package dev.codesupport.testutils.domain;

import dev.codesupport.web.common.domain.Validatable;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;

import java.util.Collections;
import java.util.List;

public class MockValidatable implements Validatable<Long> {
    @Override
    public List<ValidationIssue> validate() {
        return Collections.emptyList();
    }

    @Override
    public Long getId() {
        return 1L;
    }
}
