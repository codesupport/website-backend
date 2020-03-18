package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.security.models.DiscordOAuthTokenRequest;
import dev.codesupport.web.common.service.http.ObjectToUrlEncodedConverter;
import dev.codesupport.web.common.service.http.RestTemplateResponseErrorHandler;
import dev.codesupport.web.common.service.service.CrudOperations;
import org.junit.After;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ApplicationConfigurationTest {

    @Before
    public void setUp() {
        DiscordOAuthTokenRequest.setClient_id(null);
        DiscordOAuthTokenRequest.setSecret(null);
        DiscordOAuthTokenRequest.setRedirect_uri(null);
    }

    @After
    public void tearDown() {
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
        DiscordAppProperties mockDiscordAppProperties = mock(DiscordAppProperties.class);

        doReturn(true)
                .when(mockDiscordAppProperties)
                .isValid();

        new ApplicationConfiguration(mockContext, mockDiscordAppProperties);

        Object actualContext = ReflectionTestUtils.getField(CrudOperations.class, "context");

        assertEquals(mockContext, actualContext);
    }

    @Test
    public void shouldNotHaveDiscordAppPropertiesSetOnTokenRequestModel() {
        String code = "myCode";

        DiscordOAuthTokenRequest request = DiscordOAuthTokenRequest.create(code);
        Object clientId = defaultIfNull(ReflectionTestUtils.getField(request, "client_id"), "");
        Object secret = defaultIfNull(ReflectionTestUtils.getField(request, "secret"), "");
        Object redirectUri = defaultIfNull(ReflectionTestUtils.getField(request, "redirect_uri"), "");

        String expected = "::";
        String actual = clientId + ":" + secret + ":" + redirectUri;

        assertEquals(expected, actual);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowConfigurationExceptionIfDiscordAppPropertiesNotSet() {
        ApplicationContext mockContext = mock(ApplicationContext.class);
        DiscordAppProperties mockDiscordAppProperties = mock(DiscordAppProperties.class);

        doReturn(false)
                .when(mockDiscordAppProperties)
                .isValid();

        new ApplicationConfiguration(mockContext, mockDiscordAppProperties);
    }

    @Test
    public void shouldHaveDiscordAppPropertiesSetOnTokenRequestModel() {
        String code = "myCode";
        String expectedClient = "myClient";
        String expectedSecret = "mySecret";
        String expectedRedirect = "myRedirect";

        ApplicationContext mockContext = mock(ApplicationContext.class);
        DiscordAppProperties mockDiscordAppProperties = mock(DiscordAppProperties.class);

        doReturn(true)
                .when(mockDiscordAppProperties)
                .isValid();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(expectedClient)
                .when(mockDiscordAppProperties)
                .getClientId();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(expectedSecret)
                .when(mockDiscordAppProperties)
                .getSecret();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(expectedRedirect)
                .when(mockDiscordAppProperties)
                .getRedirectUri();

        new ApplicationConfiguration(mockContext, mockDiscordAppProperties);

        DiscordOAuthTokenRequest request = DiscordOAuthTokenRequest.create(code);
        Object clientId = ReflectionTestUtils.getField(request, "client_id");
        Object secret = ReflectionTestUtils.getField(request, "secret");
        Object redirectUri = ReflectionTestUtils.getField(request, "redirect_uri");

        String expected = expectedClient + ":" + expectedSecret + ":" + expectedRedirect;
        String actual = clientId + ":" + secret + ":" + redirectUri;

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectPasswordEncoderType() {
        ApplicationContext mockContext = mock(ApplicationContext.class);
        DiscordAppProperties mockDiscordAppProperties = mock(DiscordAppProperties.class);

        doReturn(true)
                .when(mockDiscordAppProperties)
                .isValid();

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext, mockDiscordAppProperties);

        PasswordEncoder actual = configuration.passwordEncoder();

        assertTrue(actual instanceof BCryptPasswordEncoder);
    }

    @Test
    public void shouldReturnCorrectWrapperFactoryType() {
        ApplicationContext mockContext = mock(ApplicationContext.class);
        DiscordAppProperties mockDiscordAppProperties = mock(DiscordAppProperties.class);

        doReturn(true)
                .when(mockDiscordAppProperties)
                .isValid();

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext, mockDiscordAppProperties);

        ClientHttpRequestFactory requestFactory = configuration.clientHttpRequestFactory();

        assertTrue(requestFactory instanceof BufferingClientHttpRequestFactory);
    }

    @Test
    public void shouldReturnCorrectInteriorFactoryType() {
        ApplicationContext mockContext = mock(ApplicationContext.class);
        DiscordAppProperties mockDiscordAppProperties = mock(DiscordAppProperties.class);

        doReturn(true)
                .when(mockDiscordAppProperties)
                .isValid();

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext, mockDiscordAppProperties);

        ClientHttpRequestFactory requestFactory = configuration.clientHttpRequestFactory();

        ClientHttpRequestFactory actual = (ClientHttpRequestFactory) ReflectionTestUtils.getField(requestFactory, "requestFactory");

        assertTrue(actual instanceof SimpleClientHttpRequestFactory);
    }

    @Test
    public void shouldCorrectlySetInteriorFactoryTimeoutProperty() {
        ApplicationContext mockContext = mock(ApplicationContext.class);
        DiscordAppProperties mockDiscordAppProperties = mock(DiscordAppProperties.class);

        doReturn(true)
                .when(mockDiscordAppProperties)
                .isValid();

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext, mockDiscordAppProperties);

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
        DiscordAppProperties mockDiscordAppProperties = mock(DiscordAppProperties.class);

        doReturn(true)
                .when(mockDiscordAppProperties)
                .isValid();

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext, mockDiscordAppProperties);

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
        DiscordAppProperties mockDiscordAppProperties = mock(DiscordAppProperties.class);

        doReturn(true)
                .when(mockDiscordAppProperties)
                .isValid();

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext, mockDiscordAppProperties);

        ClientHttpRequestFactory mockFactory = mock(ClientHttpRequestFactory.class);

        RestTemplate restTemplate = configuration.restTemplate(mockFactory);

        assertTrue(restTemplate.getErrorHandler() instanceof RestTemplateResponseErrorHandler);
    }

    //SameParameterValue - Don't care
    @SuppressWarnings("SameParameterValue")
    private Object defaultIfNull(Object o, Object def) {
        return o == null ? def : o;
    }

}
