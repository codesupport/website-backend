package dev.codesupport.testutils.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import dev.codesupport.web.common.domain.Validatable;
import dev.codesupport.web.common.service.data.validation.ValidationIssue;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockDomain implements Validatable<Long> {

    private Long id;
    private String propertyA;

    @Override
    public List<ValidationIssue> validate() {
        return Collections.emptyList();
    }
}
