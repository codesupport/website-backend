package dev.codesupport.web.common.security;

import dev.codesupport.web.api.data.entity.PermissionEntity;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.exception.DisabledUserException;
import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.security.hashing.HashingUtility;
import dev.codesupport.web.common.security.jwt.JwtUtility;
import dev.codesupport.web.common.security.models.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Service for performing logic to check authorizations.
 */
@Component
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserRepository userRepository;
    private final HashingUtility hashingUtility;
    private final JwtUtility jwtUtility;

    @Autowired
    public AuthorizationServiceImpl(
            UserRepository userRepository,
            HashingUtility hashingUtility,
            JwtUtility jwtUtility
    ) {
        this.userRepository = userRepository;
        this.hashingUtility = hashingUtility;
        this.jwtUtility = jwtUtility;
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

}
