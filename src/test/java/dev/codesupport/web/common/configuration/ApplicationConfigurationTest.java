package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.service.http.client.ObjectToUrlEncodedConverter;
import dev.codesupport.web.common.service.http.client.RestTemplateResponseErrorHandler;
import org.junit.Test;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ApplicationConfigurationTest {

    @Test
    public void shouldReturnCorrectPasswordEncoderType() {
        ApplicationConfiguration configuration = new ApplicationConfiguration();

        PasswordEncoder actual = configuration.passwordEncoder();

        assertTrue(actual instanceof BCryptPasswordEncoder);
    }

    @Test
    public void shouldReturnCorrectWrapperFactoryType() {
        ApplicationConfiguration configuration = new ApplicationConfiguration();

        ClientHttpRequestFactory requestFactory = configuration.clientHttpRequestFactory();

        assertTrue(requestFactory instanceof BufferingClientHttpRequestFactory);
    }

    @Test
    public void shouldReturnCorrectInteriorFactoryType() {
        ApplicationConfiguration configuration = new ApplicationConfiguration();

        ClientHttpRequestFactory requestFactory = configuration.clientHttpRequestFactory();

        ClientHttpRequestFactory actual = (ClientHttpRequestFactory) ReflectionTestUtils.getField(requestFactory, "requestFactory");

        assertTrue(actual instanceof SimpleClientHttpRequestFactory);
    }

    @Test
    public void shouldCorrectlySetInteriorFactoryTimeoutProperty() {
        ApplicationConfiguration configuration = new ApplicationConfiguration();

        ClientHttpRequestFactory requestFactory = configuration.clientHttpRequestFactory();

        ClientHttpRequestFactory interiorFactory = (ClientHttpRequestFactory) ReflectionTestUtils.getField(requestFactory, "requestFactory");

        //ConstantConditions - THis is fine for the purposes of this test.
        //noinspection ConstantConditions
        int actual = (int) ReflectionTestUtils.getField(interiorFactory, "connectTimeout");

        assertEquals(5000, actual);
    }

    @Test
    public void shouldReturnRestTemplateWithUrlEncodedConverter() {
        ApplicationConfiguration configuration = new ApplicationConfiguration();

        ClientHttpRequestFactory mockFactory = mock(ClientHttpRequestFactory.class);

        RestTemplate restTemplate = configuration.restTemplate(mockFactory);

        List<HttpMessageConverter<?>> messageConverterList = restTemplate.getMessageConverters();

        boolean hasConverter = false;

        for (HttpMessageConverter<?> messageConverter : messageConverterList) {
            if (messageConverter instanceof ObjectToUrlEncodedConverter) {
                hasConverter = true;
                break;
            }
        }

        assertTrue(hasConverter);
    }

    @Test
    public void shouldReturnRestTemplateWithCorrectErrorHandler() {
        ApplicationConfiguration configuration = new ApplicationConfiguration();

        ClientHttpRequestFactory mockFactory = mock(ClientHttpRequestFactory.class);

        RestTemplate restTemplate = configuration.restTemplate(mockFactory);

        assertTrue(restTemplate.getErrorHandler() instanceof RestTemplateResponseErrorHandler);
    }

}
