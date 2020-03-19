package dev.codesupport.testutils.domain;

import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockIdentifiableDomain implements IdentifiableDomain<Long> {

    private Long id;
    private String propertyA;

}
