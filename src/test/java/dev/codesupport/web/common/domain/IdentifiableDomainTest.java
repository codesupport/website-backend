package dev.codesupport.web.common.domain;

import dev.codesupport.testutils.domain.MockIdentifiableDomain;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IdentifiableDomainTest {

    @Test
    public void shouldHaveAGetIdMethod() {
        IdentifiableDomain<Long> identifiableDomain = new MockIdentifiableDomain();

        Long expected = 1L;

        assertEquals(expected, identifiableDomain.getId());
    }
}
