package dev.codesupport.web.common.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@ToString
public class DiscordOAuthTokenRequest implements Serializable {

    @Setter
    private static String client_id;
    @Setter
    private static String secret;
    @Setter
    private static String redirect_uri;

    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("grant_type")
    private final String grantType = "authorization_code";
    private final String code;
    @JsonProperty("redirect_uri")
    private String redirectUri;
    private final String scope = "identify guilds guild.join";

    DiscordOAuthTokenRequest(String code) {
        this.code = code;
        this.clientId = client_id;
        this.clientSecret = secret;
        this.redirectUri = redirect_uri;
    }

    public static DiscordOAuthTokenRequest create(String code) {
        return new DiscordOAuthTokenRequest(code);
    }

}
