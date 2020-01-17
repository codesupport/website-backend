package dev.codesupport.web.common.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DiscordUser implements Serializable {

    private String id;
    private String username;
    private String discriminator;
    private String avatar;
    private boolean bot;
    private boolean system;
    @JsonProperty("mfa_enabled")
    private boolean mfaEnabled;
    private String locale;
    private Integer flags;
    @JsonProperty("premium_type")
    private Integer premiumType;

}
