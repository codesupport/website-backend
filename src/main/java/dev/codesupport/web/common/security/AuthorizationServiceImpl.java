package dev.codesupport.web.common.security;

import dev.codesupport.web.api.data.entity.PermissionEntity;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.exception.DisabledUserException;
import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.security.hashing.HashingUtility;
import dev.codesupport.web.common.security.jwt.JwtUtility;
import dev.codesupport.web.common.security.models.DiscordOAuthTokenRequest;
import dev.codesupport.web.common.security.models.DiscordOAuthTokenResponse;
import dev.codesupport.web.common.security.models.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

/**
 * Service for performing logic to check authorizations.
 */
@Service
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserRepository userRepository;
    private final HashingUtility hashingUtility;
    private final JwtUtility jwtUtility;
    private final RestTemplate restTemplate;

    @Autowired
    public AuthorizationServiceImpl(
            UserRepository userRepository,
            HashingUtility hashingUtility,
            JwtUtility jwtUtility,
            RestTemplate restTemplate
    ) {
        this.userRepository = userRepository;
        this.hashingUtility = hashingUtility;
        this.jwtUtility = jwtUtility;
        this.restTemplate = restTemplate;
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

    @Override
    //TODO: Create HTTPRequest client before making unit tests.
    public boolean linkDiscord(String code) {
        boolean accountLinked = false;
        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = userAuthentication.getPrincipal();
        UserDetails userDetails;
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        } else {
            throw new InvalidUserException("Authorization not valid");
        }

        String discordOAuthTokenUri = "https://discordapp.com/api/oauth2/token";

        DiscordOAuthTokenRequest tokenRequest = DiscordOAuthTokenRequest.create(code);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        HttpEntity<DiscordOAuthTokenRequest> requestEntity = new HttpEntity<>(tokenRequest, httpHeaders);

        ResponseEntity<DiscordOAuthTokenResponse> responseEntity = restTemplate
                .exchange(
                        discordOAuthTokenUri,
                        HttpMethod.POST,
                        requestEntity,
                        DiscordOAuthTokenResponse.class
                );

        DiscordOAuthTokenResponse response = responseEntity.getBody();

        if (response != null) {
            String accessToken = response.getAccessToken();

            accountLinked = true;
            log.debug("Can link " + userDetails.getEmail() + " to discord account.");
        }

        return accountLinked;
    }

}
