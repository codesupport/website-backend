package dev.codesupport.web.common.security.models;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;

public class DiscordOAuthTokenRequestTest {

    @Test
    public void shouldHaveDefaultValueForGrantType() {
        String code = "code";

        DiscordOAuthTokenRequest request = new DiscordOAuthTokenRequest(code);

        String expected = "authorization_code";
        String actual = request.getGrantType();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldHaveDefaultValueForScope() {
        String code = "code";

        DiscordOAuthTokenRequest request = new DiscordOAuthTokenRequest(code);

        String expected = "identify guilds guild.join";
        String actual = request.getScope();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCreateObjectWithStaticValues() {
        String code = "code";

        String clientId = "my_client_id";
        String secret = "my_secret";
        String redirectUri = "my_redirect_uri";

        DiscordOAuthTokenRequest.setClient_id(clientId);
        DiscordOAuthTokenRequest.setSecret(secret);
        DiscordOAuthTokenRequest.setRedirect_uri(redirectUri);

        DiscordOAuthTokenRequest expected = new DiscordOAuthTokenRequest(code);
        ReflectionTestUtils.setField(expected, "clientId", clientId);
        ReflectionTestUtils.setField(expected, "clientSecret", secret);
        ReflectionTestUtils.setField(expected, "redirectUri", redirectUri);

        DiscordOAuthTokenRequest actual = DiscordOAuthTokenRequest.create(code);

        assertEquals(expected, actual);
    }

}
