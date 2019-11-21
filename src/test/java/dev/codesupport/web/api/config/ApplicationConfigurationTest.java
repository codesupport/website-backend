package dev.codesupport.web.api.config;

import dev.codesupport.web.common.service.service.CrudOperations;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class ApplicationConfigurationTest {

    @Before
    public void setup() {
        CrudOperations.setContext(null);
    }

    @Test
    public void shouldNotHaveContextSetForCrudOperations() {
        Object context = ReflectionTestUtils.getField(CrudOperations.class, "context");

        assertNull(context);
    }

    @Test
    public void shouldHaveContextSetForCrudOperations() {
        ApplicationContext mockContext = mock(ApplicationContext.class);

        new ApplicationConfiguration(mockContext);

        Object actualContext = ReflectionTestUtils.getField(CrudOperations.class, "context");

        assertEquals(mockContext, actualContext);
    }
}
