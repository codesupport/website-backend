package dev.codesupport.web.common.service.service;

import org.junit.Test;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RestResponseTest {

    @Test
    public void shouldSetDefaultStatusByDefault() {
        RestResponse<Serializable> restResponse = new RestResponse<>();

        assertEquals(RestStatus.OK, restResponse.getStatus());
    }

    @Test
    public void shouldSetReferenceIdByDefault() {
        RestResponse<Serializable> restResponse = new RestResponse<>();

        assertNotNull(restResponse.getReferenceId());
    }

    @Test
    public void shouldSetResponseWithDefaultProperties() {
        List<String> stringList = Collections.singletonList(
                "hello"
        );

        RestResponse<String> actual = new RestResponse<>(stringList);

        RestResponse<String> expected = new RestResponse<>(stringList);
        expected.setStatus(RestStatus.OK);
        //This is a randomly generated value, and we don't care for the sake of this test
        expected.setReferenceId(actual.getReferenceId());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldSetResponseWithDefaultPropertiesWithSingleObject() {
        String string = "hello";

        RestResponse<String> actual = new RestResponse<>(string);

        RestResponse<String> expected = new RestResponse<>(string);
        expected.setStatus(RestStatus.OK);
        //This is a randomly generated value, and we don't care for the sake of this test
        expected.setReferenceId(actual.getReferenceId());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlySetResponseWithSingleObject() {
        String string = "hello";

        RestResponse<String> actual = new RestResponse<>(string);

        assertEquals(Collections.singletonList(string), actual.getResponse());
    }

    @Test
    public void shouldStaticallyCreateRestResponseWithDefaultProperties() {
        List<String> stringList = Collections.singletonList(
                "hello"
        );

        RestResponse<String> actual = RestResponse.restResponse(stringList);

        RestResponse<String> expected = new RestResponse<>(stringList);
        expected.setStatus(RestStatus.OK);
        //This is a randomly generated value, and we don't care for the sake of this test
        expected.setReferenceId(actual.getReferenceId());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldStaticallyCreateRestResponseWithDefaultPropertiesWithSingleObject() {
        String string = "hello";

        RestResponse<String> actual = RestResponse.restResponse(string);

        RestResponse<String> expected = new RestResponse<>(string);
        expected.setStatus(RestStatus.OK);
        //This is a randomly generated value, and we don't care for the sake of this test
        expected.setReferenceId(actual.getReferenceId());

        assertEquals(expected, actual);
    }
}
