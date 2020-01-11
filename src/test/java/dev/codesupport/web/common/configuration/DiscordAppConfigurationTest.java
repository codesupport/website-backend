package dev.codesupport.web.common.configuration;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class DiscordAppConfigurationTest {

    @Test
    public void shouldReturnFalseForIsValidIfClientIdNotSet() {
        String secret = "secret";
        String redirectUri = "redirect";

        DiscordAppConfiguration configuration = new DiscordAppConfiguration();

        configuration.setSecret(secret);
        configuration.setRedirectUri(redirectUri);

        assertFalse(configuration.isValid());
    }

    @Test
    public void shouldReturnFalseForIsValidIfSecretNotSet() {
        String clientId = "client";
        String redirectUri = "redirect";

        DiscordAppConfiguration configuration = new DiscordAppConfiguration();

        configuration.setClientId(clientId);
        configuration.setRedirectUri(redirectUri);

        assertFalse(configuration.isValid());
    }

    @Test
    public void shouldReturnFalseForIsValidIfRedirectUriNotSet() {
        String clientId = "client";
        String secret = "secret";

        DiscordAppConfiguration configuration = new DiscordAppConfiguration();

        configuration.setClientId(clientId);
        configuration.setSecret(secret);

        assertFalse(configuration.isValid());
    }

    @Test
    public void shouldReturnTrueForIsValidIfAllPropertiesAreSet() {
        String clientId = "client";
        String secret = "secret";
        String redirectUri = "redirect";

        DiscordAppConfiguration configuration = new DiscordAppConfiguration();

        configuration.setClientId(clientId);
        configuration.setSecret(secret);
        configuration.setRedirectUri(redirectUri);

        assertTrue(configuration.isValid());
    }
}
