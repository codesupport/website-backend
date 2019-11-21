package dev.codesupport.web.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.codesupport.testutils.domain.MockDomain;
import dev.codesupport.testutils.entity.MockEntity;
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

        MockDomain domain = new MockDomain(id, propertyValue);

        MockEntity expected = new MockEntity(id, propertyValue);
        MockEntity actual = MappingUtils.convertToType(domain, MockEntity.class);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyConvertListOfObjectsToExpectedType() {
        Long id = 1L;
        String propertyValue = "property";

        List<MockDomain> domains = Collections.singletonList(
                new MockDomain(id, propertyValue)
        );

        List<MockEntity> expected = Collections.singletonList(
                new MockEntity(id, propertyValue)
        );
        List<MockEntity> actual = MappingUtils.convertToType(domains, MockEntity.class);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyConvertFromJsonStringToExpectedObject() throws IOException {
        Long id = 1L;
        String propertyValue = "property";

        String jsonString = "{ \"id\" : 1, \"propertyA\" : \"property\" }";

        MockDomain expected = new MockDomain(id, propertyValue);
        MockDomain actual = MappingUtils.convertFromJson(jsonString, MockDomain.class);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyConvertFromJsonStringToExpectedObjectList() throws IOException {
        Long id = 1L;
        String propertyValue = "property";

        String jsonString = "[ { \"id\" : 1, \"propertyA\" : \"property\" } ]";

        List<MockDomain> expected = Collections.singletonList(
                new MockDomain(id, propertyValue)
        );
        List<MockDomain> actual = MappingUtils.convertFromJsonList(jsonString, MockDomain.class);

        assertEquals(expected, actual);
    }
}
