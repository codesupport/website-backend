package dev.codesupport.web.common.security;

import com.google.common.net.HttpHeaders;
import dev.codesupport.web.api.data.entity.PermissionEntity;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.exception.DisabledUserException;
import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.security.hashing.HashingUtility;
import dev.codesupport.web.common.security.jwt.JwtUtility;
import dev.codesupport.web.common.security.models.DiscordOAuthTokenRequest;
import dev.codesupport.web.common.security.models.DiscordOAuthTokenResponse;
import dev.codesupport.web.common.security.models.DiscordUser;
import dev.codesupport.web.common.security.models.UserDetails;
import dev.codesupport.web.common.service.http.HttpClient;
import dev.codesupport.web.common.service.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for performing logic to check authorizations.
 */
@Component
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserRepository userRepository;
    private final HashingUtility hashingUtility;
    private final JwtUtility jwtUtility;
    private final HttpClient httpClient;

    @Autowired
    public AuthorizationServiceImpl(
            UserRepository userRepository,
            HashingUtility hashingUtility,
            JwtUtility jwtUtility,
            HttpClient httpClient
    ) {
        this.userRepository = userRepository;
        this.hashingUtility = hashingUtility;
        this.jwtUtility = jwtUtility;
        this.httpClient = httpClient;
    }

    /**
     * Creates a token if the given credentials are valid.
     *
     * @param email    The email for the credentials.
     * @param password The clear text password for the credentials
     * @return A token string if the credentials are valid
     * @throws InvalidUserException If the authorization could not be completed for the given credentials.
     */
    @Override
    public String createTokenForEmailAndPassword(String email, String password) {
        UserDetails userDetails;

        try {
            userDetails = getUserDetailsByEmail(email);

            if (hashingUtility.verifyPassword(password, userDetails.getPassword())) {
                if (userDetails.isDisabled()) {
                    throw new DisabledUserException("User is disabled.");
                }
            } else {
                throw new InvalidUserException("Invalid user credentials.");
            }
        } catch (InvalidUserException | DisabledUserException e) {
            throw new InvalidUserException("Could not authenticate user.", e);
        }

        return jwtUtility.generateToken(userDetails.getAlias(), userDetails.getEmail());
    }

    /**
     * Gets {@link UserDetails} associated with the given email if it exists.
     *
     * @param email The email associated to the desired {@link UserDetails}
     * @return The {@link UserDetails} associated to the given email, if it exists.
     * @throws InvalidUserException If the email does not exist in the repository.
     */
    @Override
    public UserDetails getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new InvalidUserException("User does not exist for given email.");
        }

        return new UserDetails(
                userEntity.getAlias(),
                userEntity.getHashPassword(),
                userEntity.getEmail(),
                userEntity.getPermission().stream().map(PermissionEntity::getCode).collect(Collectors.toSet()),
                userEntity.isDisabled()
        );
    }

    /**
     * Attempts to look up and add a user's discord ID to their local profile
     *
     * @param code The code provided by DiscordApi to get user access token
     */
    @Override
    public void linkDiscord(String code) {
        String email = getUserEmailFromAuthorization();

        DiscordOAuthTokenResponse tokenResponse = getTokenFromDiscordApi(code);

        DiscordUser discordUser = getDiscordUserDetailsFromDiscordApi(tokenResponse.getAccessToken());

        saveDiscordIdToUser(email, discordUser.getId());
    }

    /**
     * Saves the given discordId to the user with the given email
     *
     * @param email     Email associated to user to link with discordId
     * @param discordId The Discord Id to link to the user
     */
    void saveDiscordIdToUser(String email, String discordId) {
        UserEntity userEntity = userRepository.findByEmail(email);
        userEntity.setDiscordId(discordId);
        userRepository.save(userEntity);
    }

    /**
     * Get user details from Discord's API
     *
     * @param accessToken The user access token to use to retrieve data
     * @return The {@link DiscordUser} details associated with the access token
     */
    DiscordUser getDiscordUserDetailsFromDiscordApi(String accessToken) {
        String discordOAuthTokenUri = "https://discordapp.com/api/users/@me";

        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        return httpClient
                .rest(String.class, DiscordUser.class)
                .withUrl(discordOAuthTokenUri)
                .withHeaders(httpHeaders)
                .sync(HttpMethod.GET);
    }

    /**
     * Get the access token from Discords Api for the user associated with the given code
     *
     * @param code The code used to acquire an access token.
     * @return The API response in a {@link DiscordOAuthTokenResponse} object
     */
    DiscordOAuthTokenResponse getTokenFromDiscordApi(String code) {
        String discordOAuthTokenUri = "https://discordapp.com/api/oauth2/token";

        DiscordOAuthTokenRequest tokenRequest = DiscordOAuthTokenRequest.create(code);

        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        return httpClient
                .rest(DiscordOAuthTokenRequest.class, DiscordOAuthTokenResponse.class)
                .withUrl(discordOAuthTokenUri)
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
    String getUserEmailFromAuthorization() {
        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = userAuthentication.getPrincipal();
        UserDetails userDetails;
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        } else {
            throw new InvalidUserException("Authorization not valid");
        }

        return userDetails.getEmail();
    }

}
