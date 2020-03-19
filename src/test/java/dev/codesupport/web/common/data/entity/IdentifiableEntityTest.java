package dev.codesupport.web.common.data.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class IdentifiableEntityTest {

    @Test
    public void shouldHaveAGetIdMethod() {
        //unchecked - This is fine for the purposes of this test
        //noinspection unchecked
        IdentifiableEntity<Long> mockEntity = mock(IdentifiableEntity.class);

        Long expected = 1L;

        doReturn(expected)
                .when(mockEntity)
                .getId();

        assertEquals(expected, mockEntity.getId());
    }

}
