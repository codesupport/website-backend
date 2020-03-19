package dev.codesupport.web.common.service.http.client;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RestRequestTest {

    private static class TestRestRequest<S extends Serializable, T extends Serializable> extends RestRequest<S, T> {

        public TestRestRequest() {
            super();
        }

        public TestRestRequest(Class<S> requestClass, Class<T> responseClass) {
            super(requestClass, responseClass);
        }

        @Override
        public T sync(HttpMethod httpMethod) {
            return null;
        }
    }

    @Test
    public void shouldCorrectlyInitializeHeaders() {
        RestRequest<String, String> testRestRequest = new TestRestRequest<>();

        Map<String, String> expected = Collections.singletonMap(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
        );

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, String> actual = (Map<String, String>) ReflectionTestUtils.getField(testRestRequest, "headers");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyInitializeParameters() {
        RestRequest<String, String> testRestRequest = new TestRestRequest<>();

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, String> actual = (Map<String, String>) ReflectionTestUtils.getField(testRestRequest, "parameters");

        Map<String, String> expected = Collections.emptyMap();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyRequestObjectClassType() {
        RestRequest<String, Long> testRestRequest = new TestRestRequest<>(String.class, Long.class);

        //rawtypes - This is fine for the purposes of this test.
        //noinspection rawtypes
        Class actual = (Class) ReflectionTestUtils.getField(testRestRequest, "requestClass");

        assertEquals(String.class, actual);
    }

    @Test
    public void shouldCorrectlyResponseObjectClassType() {
        RestRequest<String, Long> testRestRequest = new TestRestRequest<>(String.class, Long.class);

        //rawtypes - This is fine for the purposes of this test.
        //noinspection rawtypes
        Class actual = (Class) ReflectionTestUtils.getField(testRestRequest, "responseClass");

        assertEquals(Long.class, actual);
    }

    @Test
    public void shouldCorrectlySetUrl() {
        String expected = "http://www.google.com";

        RestRequest<String, Long> testRestRequest = new TestRestRequest<>(String.class, Long.class);

        testRestRequest.withUrl(expected);

        String actual = (String) ReflectionTestUtils.getField(testRestRequest, "url");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlySetParameter() {
        String name = "myParam";
        String value = "myValue";

        RestRequest<String, Long> testRestRequest = new TestRestRequest<>(String.class, Long.class);

        testRestRequest.withParameter(name, value);

        Map<String, String> expected = Collections.singletonMap(name, value);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, String> actual = (Map<String, String>) ReflectionTestUtils.getField(testRestRequest, "parameters");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlySetParameters() {
        RestRequest<String, Long> testRestRequest = new TestRestRequest<>(String.class, Long.class);

        Map<String, String> expected = new HashMap<>();
        expected.put("param1", "value1");
        expected.put("param2", "value2");

        testRestRequest.withParameters(expected);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, String> actual = (Map<String, String>) ReflectionTestUtils.getField(testRestRequest, "parameters");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlySetHeader() {
        String name = "User-Agent";
        String value = "myValue";

        RestRequest<String, Long> testRestRequest = new TestRestRequest<>(String.class, Long.class);

        testRestRequest.withHeader(name, value);

        Map<String, String> expected = Collections.singletonMap(name, value);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, String> actual = (Map<String, String>) ReflectionTestUtils.getField(testRestRequest, "headers");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlySetHeaders() {
        RestRequest<String, Long> testRestRequest = new TestRestRequest<>(String.class, Long.class);

        Map<String, String> expected = new HashMap<>();
        expected.put("User-Agent", "value1");
        expected.put("param2", "value2");

        testRestRequest.withHeaders(expected);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        Map<String, String> actual = (Map<String, String>) ReflectionTestUtils.getField(testRestRequest, "headers");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlySetPayload() {
        RestRequest<String, Long> testRestRequest = new TestRestRequest<>(String.class, Long.class);

        String expected = "Hello world";

        testRestRequest.withPayload(expected);

        String actual = (String) ReflectionTestUtils.getField(testRestRequest, "requestObject");

        assertEquals(expected, actual);
    }

}
