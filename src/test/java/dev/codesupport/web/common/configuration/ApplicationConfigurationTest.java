package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.service.http.ObjectToUrlEncodedConverter;
import dev.codesupport.web.common.service.http.RestTemplateResponseErrorHandler;
import dev.codesupport.web.common.service.service.CrudOperations;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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

    @Test
    public void shouldReturnCorrectPasswordEncoderType() {
        ApplicationContext mockContext = mock(ApplicationContext.class);

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext);

        PasswordEncoder actual = configuration.passwordEncoder();

        assertTrue(actual instanceof BCryptPasswordEncoder);
    }

    @Test
    public void shouldReturnCorrectWrapperFactoryType() {
        ApplicationContext mockContext = mock(ApplicationContext.class);

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext);

        ClientHttpRequestFactory requestFactory = configuration.clientHttpRequestFactory();

        assertTrue(requestFactory instanceof BufferingClientHttpRequestFactory);
    }

    @Test
    public void shouldReturnCorrectInteriorFactoryType() {
        ApplicationContext mockContext = mock(ApplicationContext.class);

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext);

        ClientHttpRequestFactory requestFactory = configuration.clientHttpRequestFactory();

        ClientHttpRequestFactory actual = (ClientHttpRequestFactory) ReflectionTestUtils.getField(requestFactory, "requestFactory");

        assertTrue(actual instanceof SimpleClientHttpRequestFactory);
    }

    @Test
    public void shouldCorrectlySetInteriorFactoryTimeoutProperty() {
        ApplicationContext mockContext = mock(ApplicationContext.class);

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext);

        ClientHttpRequestFactory requestFactory = configuration.clientHttpRequestFactory();

        ClientHttpRequestFactory interiorFactory = (ClientHttpRequestFactory) ReflectionTestUtils.getField(requestFactory, "requestFactory");

        //ConstantConditions - THis is fine for the purposes of this test.
        //noinspection ConstantConditions
        int actual = (int) ReflectionTestUtils.getField(interiorFactory, "connectTimeout");

        assertEquals(5000, actual);
    }

    @Test
    public void shouldReturnRestTemplateWithUrlEncodedConverter() {
        ApplicationContext mockContext = mock(ApplicationContext.class);

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext);

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
        ApplicationContext mockContext = mock(ApplicationContext.class);

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext);

        ClientHttpRequestFactory mockFactory = mock(ClientHttpRequestFactory.class);

        RestTemplate restTemplate = configuration.restTemplate(mockFactory);

        assertTrue(restTemplate.getErrorHandler() instanceof RestTemplateResponseErrorHandler);
    }

}
