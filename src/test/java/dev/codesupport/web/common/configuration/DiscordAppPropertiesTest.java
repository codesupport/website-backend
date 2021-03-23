package dev.codesupport.web.common.configuration;

import dev.codesupport.web.api.ApiServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "security.discord.client-id=1234",
                "security.discord.secret=4321",
                "security.discord.redirect-id=example.com"
        },
        classes = ApiServiceApplication.class
)
public class DiscordAppPropertiesTest {

    //unused - This is autowired, IDE can't see for some reason
    @SuppressWarnings("unused")
    @Autowired
    private DiscordAppProperties discordAppProperties;

    @Test
    public void shouldReturnFalseForIsValidIfClientIdNotSet() {
        String secret = "secret";
        String redirectUri = "redirect";

        DiscordAppProperties configuration = new DiscordAppProperties();

        configuration.setSecret(secret);
        configuration.setRedirectUri(redirectUri);

        assertFalse(configuration.isValid());
    }

    @Test
    public void shouldReturnFalseForIsValidIfSecretNotSet() {
        String clientId = "client";
        String redirectUri = "redirect";

        DiscordAppProperties configuration = new DiscordAppProperties();

        configuration.setClientId(clientId);
        configuration.setRedirectUri(redirectUri);

        assertFalse(configuration.isValid());
    }

    @Test
    public void shouldReturnFalseForIsValidIfRedirectUriNotSet() {
        String clientId = "client";
        String secret = "secret";

        DiscordAppProperties configuration = new DiscordAppProperties();

        configuration.setClientId(clientId);
        configuration.setSecret(secret);

        assertFalse(configuration.isValid());
    }

    @Test
    public void shouldReturnTrueForIsValidIfAllPropertiesAreSet() {
        String apiUrl = "http://www.example.com";
        String clientId = "client";
        String secret = "secret";
        String redirectUri = "redirect";

        DiscordAppProperties configuration = new DiscordAppProperties();

        configuration.setApiHost(apiUrl);
        configuration.setClientId(clientId);
        configuration.setSecret(secret);
        configuration.setRedirectUri(redirectUri);

        assertTrue(configuration.isValid());
    }

    @Test
    public void shouldReadCorrectPropertiesFromFile() {
        String clientId = "1234";
        String secret = "4321";
        String redirectUri = "example.com";

        String expected = clientId + ":" + secret + ":" + redirectUri;
        String actual = discordAppProperties.getClientId() + ":" +
                discordAppProperties.getSecret() + ":" +
                discordAppProperties.getRedirectUri();

        assertEquals(expected, actual);
    }

}
