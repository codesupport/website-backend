package dev.codesupport.testutils.entity;

import dev.codesupport.web.common.service.data.entity.IdentifiableEntity;

public class MockIdentifiableEntity implements IdentifiableEntity<Long> {

    @Override
    public Long getId() {
        return 1L;
    }

}
