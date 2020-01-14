package dev.codesupport.web.common.service.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class SpringHttpClientTest {

    @Test
    public void shouldSetRestTemplate() {
        RestTemplate mockRestTemplate = mock(RestTemplate.class);

        SpringHttpClient client = new SpringHttpClient(mockRestTemplate);

        RestTemplate actual = (RestTemplate) ReflectionTestUtils.getField(client, "restTemplate");

        assertEquals(mockRestTemplate, actual);
    }

    @Test
    public void shouldReturnSpringRestRequest() {
        RestTemplate mockRestTemplate = mock(RestTemplate.class);

        SpringHttpClient client = new SpringHttpClient(mockRestTemplate);

        RestRequest<String, Long> restRequest = client.rest(String.class, Long.class);

        assertTrue(restRequest instanceof SpringHttpClient.SpringRestRequest);
    }

    @Test
    public void shouldSetRestTemplateInSpringRestRequest() {
        RestTemplate mockRestTemplate = mock(RestTemplate.class);

        SpringHttpClient client = new SpringHttpClient(mockRestTemplate);

        RestRequest<String, Long> restRequest = client.rest(String.class, Long.class);

        RestTemplate actual = (RestTemplate) ReflectionTestUtils.getField(restRequest, "restTemplate");

        assertEquals(mockRestTemplate, actual);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TestClass implements Serializable {
        private String myString;
    }

    @Test
    public void shouldDoSomethingRight() {
        //TODO: This test is too complex, break it up somehow.
        String url = "my url";
        String header = "header";
        String value = "value";
        Map<String, String> headers = Collections.singletonMap(header, value);
        Map<String, String> parameters = new HashMap<>();
        String requestObject = "hi";
        String responseString = "some string";
        String responseEntityString = "{ \"myString\": \"" + responseString + "\" }";
        HttpMethod httpMethod = HttpMethod.POST;
        org.springframework.http.HttpMethod springHttpMethod = org.springframework.http.HttpMethod.POST;

        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.put(header, Collections.singletonList(value));

        RestTemplate mockRestTemplate = mock(RestTemplate.class);

        //unchecked - This is fine for the purposes of this test.
        //noinspection unchecked
        ResponseEntity<String> mockResponseEntity = mock(ResponseEntity.class);

        doReturn(responseEntityString)
                .when(mockResponseEntity)
                .getBody();

        doReturn(mockResponseEntity)
                .when(mockRestTemplate)
                .exchange(url, springHttpMethod, new HttpEntity<>(requestObject, headerMap), String.class, parameters);

        RestRequest<String, TestClass> restRequest = new SpringHttpClient.SpringRestRequest<>(mockRestTemplate, String.class, TestClass.class);

        ReflectionTestUtils.setField(restRequest, "url", url);
        ReflectionTestUtils.setField(restRequest, "headers", headers);
        ReflectionTestUtils.setField(restRequest, "parameters", parameters);
        ReflectionTestUtils.setField(restRequest, "requestObject", requestObject);

        TestClass expected = new TestClass(responseString);

        TestClass actual = restRequest.sync(httpMethod);

        assertEquals(expected, actual);
    }

}
