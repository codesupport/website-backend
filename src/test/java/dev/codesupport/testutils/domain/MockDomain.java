package dev.codesupport.testutils.domain;

import dev.codesupport.web.common.domain.AbstractValidatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MockDomain extends AbstractValidatable<Long> {

    private Long id;
    private String propertyA;

}
