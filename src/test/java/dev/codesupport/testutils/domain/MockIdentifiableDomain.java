package dev.codesupport.testutils.domain;

import dev.codesupport.web.common.domain.IdentifiableDomain;

public class MockIdentifiableDomain implements IdentifiableDomain<Long> {

    @Override
    public Long getId() {
        return 1L;
    }

    @Override
    public void setId(Long id) {
        // Not needed for tests.
    }

}
