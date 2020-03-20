package dev.codesupport.testutils.entity;

import dev.codesupport.web.common.data.entity.IdentifiableEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockIdentifiableEntity implements IdentifiableEntity<Long> {

    private Long id;
    private String propertyA;

}
