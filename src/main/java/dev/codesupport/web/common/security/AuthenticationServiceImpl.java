package dev.codesupport.web.common.security;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.net.HttpHeaders;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.configuration.DiscordAppProperties;
import dev.codesupport.web.common.configuration.HttpSessionProperties;
import dev.codesupport.web.common.exception.DisabledUserException;
import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.security.hashing.HashingUtility;
import dev.codesupport.web.common.security.models.DiscordOAuthTokenRequest;
import dev.codesupport.web.common.security.models.DiscordOAuthTokenResponse;
import dev.codesupport.web.common.security.models.DiscordUser;
import dev.codesupport.web.common.security.models.UserDetails;
import dev.codesupport.web.common.service.http.client.HttpClient;
import dev.codesupport.web.common.service.http.client.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service for performing logic to check authorizations.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final HttpSessionProperties httpSessionProperties;
    private final DiscordAppProperties discordAppProperties;
    private final UserRepository userRepository;
    private final HashingUtility hashingUtility;
    private final HttpClient httpClient;

    private String discordAuthApiUrl;
    private String discordUserApiUrl;

    @PostConstruct
    public void init() {
        discordAuthApiUrl = discordAppProperties.getApiHost() + "/api/oauth2/token";
        discordUserApiUrl = discordAppProperties.getApiHost() + "/api/users/@me";
    }

    /**
     * Creates a token if the given credentials are valid.
     *
     * @param email    The email for the credentials.
     * @param password The clear text password for the credentials
     * @throws InvalidUserException If the authorization could not be completed for the given credentials.
     */
    @Override
    public String authenticate(String email, String password) {
        UserEntity userEntity = getUserByEmail(email);

        if (hashingUtility.verifyPassword(password, userEntity.getHashPassword())) {
            if (!userEntity.isDisabled()) {
                return createTokenCookieForUser(userEntity);
            } else {
                throw new InvalidUserException(
                        InvalidUserException.Reason.INVALID_USER,
                        new DisabledUserException("User is disabled.")
                );
            }
        } else {
            throw new InvalidUserException(InvalidUserException.Reason.INVALID_USER);
        }
    }

    @VisibleForTesting
    UserEntity getUserByEmail(String email) {
        UserEntity userEntity;
        Optional<UserEntity> optional = userRepository.findByEmailIgnoreCase(email);

        if (optional.isPresent()) {
            userEntity = optional.get();
        } else {
            throw new InvalidUserException(InvalidUserException.Reason.INVALID_USER);
        }

        return userEntity;
    }

    @VisibleForTesting
    public Optional<UserEntity> getUserByToken(String token) {
        return userRepository.findByAccessTokenIgnoreCase(token);
    }

    @VisibleForTesting
    public String createTokenCookieForUser(UserEntity userEntity) {
        userEntity.setAccessToken(RandomStringUtils.randomAlphanumeric(50));
        userEntity.setAccessTokenExpireOn(System.currentTimeMillis() + (httpSessionProperties.getCookie().getMaxAge() * 1000));
        userRepository.save(userEntity);

        String headerValue = httpSessionProperties.getCookie().getName()
                + "=" + userEntity.getAccessToken();

        if (httpSessionProperties.getCookie().isSecure()) {
            headerValue += "; Secure";
        }

        headerValue += "; HttpOnly; Path=/";

        if (httpSessionProperties.getCookie().getMaxAge() > 0) {
            headerValue += "; MaxAge=" + httpSessionProperties.getCookie().getMaxAge();
        }

        return headerValue;
    }

    /**
     * Attempts to look up and add a user's discord ID to their local profile
     *
     * @param code The code provided by DiscordApi to get user access token
     */
    @Override
    public void linkDiscord(String code) {
        String email = getUserEmailFromAuthentication();

        DiscordOAuthTokenResponse tokenResponse = getTokenFromDiscordApi(code);

        DiscordUser discordUser = getDiscordUserDetailsFromDiscordApi(tokenResponse.getAccessToken());

        saveDiscordIdToUser(email, discordUser);
    }

    /**
     * Saves the given discordId to the user with the given email
     *
     * @param email       Email associated to user to link with discordId
     * @param discordUser The {@link DiscordUser} to link to the user
     */
    @VisibleForTesting
    void saveDiscordIdToUser(String email, DiscordUser discordUser) {
        Optional<UserEntity> optional = userRepository.findByEmailIgnoreCase(email);
        if (optional.isPresent()) {
            UserEntity userEntity = optional.get();
            userEntity.setDiscordId(discordUser.getId());
            userEntity.setDiscordUsername(discordUser.getUsername() + "#" + discordUser.getDiscriminator());
            userRepository.save(userEntity);
        } else {
            throw new InvalidUserException(InvalidUserException.Reason.MISSING_USER);
        }
    }

    /**
     * Get user details from Discord's API
     *
     * @param accessToken The user access token to use to retrieve data
     * @return The {@link DiscordUser} details associated with the access token
     */
    @VisibleForTesting
    DiscordUser getDiscordUserDetailsFromDiscordApi(String accessToken) {
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        return httpClient
                .rest(String.class, DiscordUser.class)
                .withUrl(discordUserApiUrl)
                .withHeaders(httpHeaders)
                .sync(HttpMethod.GET);
    }

    /**
     * Get the access token from Discords Api for the user associated with the given code
     *
     * @param code The code used to acquire an access token.
     * @return The API response in a {@link DiscordOAuthTokenResponse} object
     */
    @VisibleForTesting
    DiscordOAuthTokenResponse getTokenFromDiscordApi(String code) {
        DiscordOAuthTokenRequest tokenRequest = new DiscordOAuthTokenRequest(
                discordAppProperties.getClientId(),
                discordAppProperties.getSecret(),
                code,
                discordAppProperties.getRedirectUri()
        );

        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        return httpClient
                .rest(DiscordOAuthTokenRequest.class, DiscordOAuthTokenResponse.class)
                .withUrl(discordAuthApiUrl)
                .withHeaders(httpHeaders)
                .withPayload(tokenRequest)
                .sync(HttpMethod.POST);
    }

    /**
     * Get the email associated with the user authentication currently in the SecurityContext
     *
     * @return The email associated to the user authentication
     * @throws InvalidUserException If there is no valid user authentication in the Security Context
     */
    @VisibleForTesting
    String getUserEmailFromAuthentication() {
        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = userAuthentication.getPrincipal();
        UserDetails userDetails;
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        } else {
            throw new InvalidUserException(InvalidUserException.Reason.INVALID_USER);
        }

        return userDetails.getEmail();
    }

}
