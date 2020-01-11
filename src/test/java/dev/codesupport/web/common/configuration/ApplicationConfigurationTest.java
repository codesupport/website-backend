package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.exception.ConfigurationException;
import dev.codesupport.web.common.security.models.DiscordOAuthTokenRequest;
import dev.codesupport.web.common.service.service.CrudOperations;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ApplicationConfigurationTest {

    @Before
    public void setup() {
        CrudOperations.setContext(null);

        DiscordOAuthTokenRequest.setClient_id(null);
        DiscordOAuthTokenRequest.setSecret(null);
        DiscordOAuthTokenRequest.setRedirect_uri(null);
    }

    @Test
    public void shouldNotHaveContextSetForCrudOperations() {
        Object context = ReflectionTestUtils.getField(CrudOperations.class, "context");

        assertNull(context);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowErrorIfDiscordAppConfigurationIsNotValid() {
        ApplicationContext mockContext = mock(ApplicationContext.class);
        DiscordAppConfiguration mockDiscordAppConfiguration = mock(DiscordAppConfiguration.class);

        doReturn(false)
                .when(mockDiscordAppConfiguration)
                .isValid();

        new ApplicationConfiguration(mockContext, mockDiscordAppConfiguration);
    }

    @Test
    public void shouldHaveContextSetForCrudOperations() {
        ApplicationContext mockContext = mock(ApplicationContext.class);
        DiscordAppConfiguration mockDiscordAppConfiguration = mock(DiscordAppConfiguration.class);

        doReturn(true)
                .when(mockDiscordAppConfiguration)
                .isValid();

        new ApplicationConfiguration(mockContext, mockDiscordAppConfiguration);

        Object actualContext = ReflectionTestUtils.getField(CrudOperations.class, "context");

        assertEquals(mockContext, actualContext);
    }

    @Test
    public void shouldReturnCorrectPasswordEncoderType() {
        ApplicationContext mockContext = mock(ApplicationContext.class);
        DiscordAppConfiguration mockDiscordAppConfiguration = mock(DiscordAppConfiguration.class);

        doReturn(true)
                .when(mockDiscordAppConfiguration)
                .isValid();

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext, mockDiscordAppConfiguration);

        PasswordEncoder actual = configuration.passwordEncoder();

        assertTrue(actual instanceof BCryptPasswordEncoder);
    }

    @Test
    public void shouldNotHaveDiscordOAuthRequestStaticsSet() {
        DiscordOAuthTokenRequest discordOAuthTokenRequest = DiscordOAuthTokenRequest.create("code");
        Object clientId = ReflectionTestUtils.getField(discordOAuthTokenRequest, "client_id");
        Object secret = ReflectionTestUtils.getField(discordOAuthTokenRequest, "secret");
        Object redirect_uri = ReflectionTestUtils.getField(discordOAuthTokenRequest, "redirect_uri");

        String actual = Stream.of(
                defaultIfNull(clientId, ""),
                defaultIfNull(secret, ""),
                defaultIfNull(redirect_uri, "")
        ).map(Object::toString).collect(Collectors.joining());

        assertEquals("", actual);
    }

    @Test
    public void shouldHaveDiscordOAuthRequestStaticsSet() {
        String clientId = "1234";
        String clientSecret = "super";
        String redirectUri = "redir";

        ApplicationContext mockContext = mock(ApplicationContext.class);

        DiscordAppConfiguration mockDiscordAppConfiguration = mock(DiscordAppConfiguration.class);

        doReturn(true)
                .when(mockDiscordAppConfiguration)
                .isValid();

        //ResultOfMethodCallIgnored - Not calling a method, making a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn(clientId)
                .when(mockDiscordAppConfiguration)
                .getClientId();

        //ResultOfMethodCallIgnored - Not calling a method, making a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn(clientSecret)
                .when(mockDiscordAppConfiguration)
                .getSecret();

        //ResultOfMethodCallIgnored - Not calling a method, making a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn(redirectUri)
                .when(mockDiscordAppConfiguration)
                .getRedirectUri();

        new ApplicationConfiguration(mockContext, mockDiscordAppConfiguration);

        DiscordOAuthTokenRequest discordOAuthTokenRequest = DiscordOAuthTokenRequest.create("code");
        Object client_id = ReflectionTestUtils.getField(discordOAuthTokenRequest, "client_id");
        Object secret = ReflectionTestUtils.getField(discordOAuthTokenRequest, "secret");
        Object redirect_uri = ReflectionTestUtils.getField(discordOAuthTokenRequest, "redirect_uri");

        String expected = clientId + clientSecret + redirectUri;

        String actual = Stream.of(
                defaultIfNull(client_id, ""),
                defaultIfNull(secret, ""),
                defaultIfNull(redirect_uri, "")
        ).map(Object::toString).collect(Collectors.joining());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnRestTemplate() {
        ApplicationContext mockContext = mock(ApplicationContext.class);
        DiscordAppConfiguration mockDiscordAppConfiguration = mock(DiscordAppConfiguration.class);

        doReturn(true)
                .when(mockDiscordAppConfiguration)
                .isValid();

        ApplicationConfiguration configuration = new ApplicationConfiguration(mockContext, mockDiscordAppConfiguration);

        RestTemplate actual = configuration.restTemplate();

        assertNotNull(actual);
    }

    //TODO: More verbose testing of rabbitTemplate() once finalized.

}
