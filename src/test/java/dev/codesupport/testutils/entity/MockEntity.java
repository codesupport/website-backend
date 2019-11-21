package dev.codesupport.testutils.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import dev.codesupport.web.common.service.data.entity.IdentifiableEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockEntity implements IdentifiableEntity<Long> {

    private Long id;
    private String propertyA;

}
