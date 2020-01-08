package dev.codesupport.web.common.security;

import dev.codesupport.web.api.data.entity.PermissionEntity;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.exception.DisabledUserException;
import dev.codesupport.web.common.exception.InvalidUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthorizationService {

    private final UserRepository userRepository;
    private final HashingUtility hashingUtility;
    private final JwtUtility jwtUtility;

    @Autowired
    public AuthorizationService(
            UserRepository userRepository,
            HashingUtility hashingUtility,
            JwtUtility jwtUtility
    ) {
        this.userRepository = userRepository;
        this.hashingUtility = hashingUtility;
        this.jwtUtility = jwtUtility;
    }

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

    UserDetails getUserDetailsByEmail(String email) {
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
