package dev.codesupport.web.common.service.data.entity;

import dev.codesupport.testutils.entity.MockIdentifiableEntity;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IdentifiableEntityTest {

    @Test
    public void shouldHaveAGetIdMethod() {
        IdentifiableEntity<Long> identifiableDomain = new MockIdentifiableEntity();

        Long expected = 1L;

        assertEquals(expected, identifiableDomain.getId());
    }

}
