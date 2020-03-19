package dev.codesupport.web.common.data.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class IdentifiableDomainTest {

    @Test
    public void shouldHaveAGetIdMethod() {
        //unchecked - This is fine for the purposes of this test
        //noinspection unchecked
        IdentifiableDomain<Long> mockDomain = mock(IdentifiableDomain.class);

        Long expected = 1L;

        doReturn(expected)
                .when(mockDomain)
                .getId();

        assertEquals(expected, mockDomain.getId());
    }
}
