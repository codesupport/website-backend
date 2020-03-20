package dev.codesupport.web.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.codesupport.testutils.domain.MockIdentifiableDomain;
import dev.codesupport.testutils.entity.MockIdentifiableEntity;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class MappingUtilsTest {

    @Test
    public void shouldReturnSameInstanceOfMapper() {
        ObjectMapper firstInstance = MappingUtils.mapper();
        ObjectMapper secondInstance = MappingUtils.mapper();

        assertSame(firstInstance, secondInstance);
    }

    @Test
    public void shouldCorrectlyConvertSingleObjectToExpectedType() {
        Long id = 1L;
        String propertyValue = "property";

        MockIdentifiableDomain domain = new MockIdentifiableDomain(id, propertyValue);

        MockIdentifiableEntity expected = new MockIdentifiableEntity(id, propertyValue);
        MockIdentifiableEntity actual = MappingUtils.convertToType(domain, MockIdentifiableEntity.class);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyConvertListOfObjectsToExpectedType() {
        Long id = 1L;
        String propertyValue = "property";

        List<MockIdentifiableDomain> domains = Collections.singletonList(
                new MockIdentifiableDomain(id, propertyValue)
        );

        List<MockIdentifiableEntity> expected = Collections.singletonList(
                new MockIdentifiableEntity(id, propertyValue)
        );
        List<MockIdentifiableEntity> actual = MappingUtils.convertToType(domains, MockIdentifiableEntity.class);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyConvertFromJsonStringToExpectedObject() throws IOException {
        Long id = 1L;
        String propertyValue = "property";

        String jsonString = "{ \"id\" : 1, \"propertyA\" : \"property\" }";

        MockIdentifiableDomain expected = new MockIdentifiableDomain(id, propertyValue);
        MockIdentifiableDomain actual = MappingUtils.convertFromJson(jsonString, MockIdentifiableDomain.class);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyConvertFromJsonStringToExpectedObjectList() throws IOException {
        Long id = 1L;
        String propertyValue = "property";

        String jsonString = "[ { \"id\" : 1, \"propertyA\" : \"property\" } ]";

        List<MockIdentifiableDomain> expected = Collections.singletonList(
                new MockIdentifiableDomain(id, propertyValue)
        );
        List<MockIdentifiableDomain> actual = MappingUtils.convertFromJsonList(jsonString, MockIdentifiableDomain.class);

        assertEquals(expected, actual);
    }
}
