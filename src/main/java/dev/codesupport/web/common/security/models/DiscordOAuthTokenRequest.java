package dev.codesupport.web.common.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DiscordOAuthTokenRequest implements Serializable {

    @JsonProperty("client_id")
    private final String clientId;
    @JsonProperty("client_secret")
    private final String clientSecret;
    @JsonProperty("grant_type")
    private final String grantType = "authorization_code";
    private final String code;
    @JsonProperty("redirect_uri")
    private final String redirectUri;
    private final String scope = "identify guilds guild.join";

}
