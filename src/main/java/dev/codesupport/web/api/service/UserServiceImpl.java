package dev.codesupport.web.api.service;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.common.security.hashing.HashingUtility;
import dev.codesupport.web.common.security.jwt.JwtUtility;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.common.util.MappingUtils;
import dev.codesupport.web.domain.TokenResponse;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserProfileStripped;
import dev.codesupport.web.domain.UserRegistration;
import dev.codesupport.web.domain.UserStripped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Handles the business logic for the various resource operations provided by the API contract endpoints.
 */
@Component
public class UserServiceImpl implements UserService {

    private final CrudOperations<UserEntity, User, Long> userCrudOperations;
    private final CrudOperations<UserEntity, UserProfileStripped, Long> userProfileCrudOperations;

    private final UserRepository userRepository;

    private final HashingUtility hashingUtility;

    private final JwtUtility jwtUtility;

    @Autowired
    UserServiceImpl(
            UserRepository userRepository,
            HashingUtility hashingUtility,
            JwtUtility jwtUtility
    ) {
        userCrudOperations = new CrudOperations<>(userRepository, UserEntity.class, User.class);
        userProfileCrudOperations = new CrudOperations<>(userRepository, UserEntity.class, UserProfileStripped.class);
        this.userRepository = userRepository;

        this.hashingUtility = hashingUtility;

        this.jwtUtility = jwtUtility;
    }

    @Override
    public UserProfile getUserProfileByAlias(String alias) {
        UserEntity userEntity;
        Optional<UserEntity> optional = userRepository.findByAliasIgnoreCase(alias);

        if (optional.isPresent()) {
            userEntity = optional.get();
        } else {
            throw new ResourceNotFoundException(ResourceNotFoundException.Reason.NOT_FOUND);
        }

        return MappingUtils.convertToType(userEntity, UserProfile.class);
    }

    @Override
    public UserProfileStripped getUserProfileById(Long id) {
        return userProfileCrudOperations.getById(id);
    }

    @Override
    public List<UserProfileStripped> findAllUserProfiles() {
        return userProfileCrudOperations.getAll();
    }

    @Override
    public UserStripped getUserById(Long id) {
        User users = userCrudOperations.getById(id);

        return MappingUtils.convertToType(users, UserStripped.class);
    }

    @Override
    public List<UserStripped> findAllUsers() {
        List<User> users = userCrudOperations.getAll();

        return MappingUtils.convertToType(users, UserStripped.class);
    }

    @Override
    public TokenResponse registerUser(UserRegistration userRegistration) {
        User user = MappingUtils.convertToType(userRegistration, User.class);
        User createdUser;

        if (!userRepository.existsByAliasIgnoreCase(user.getAlias())) {
            if (!userRepository.existsByEmailIgnoreCase(user.getEmail())) {
                user.setHashPassword(
                        hashingUtility.hashPassword(userRegistration.getPassword())
                );

                createdUser = userCrudOperations.createEntity(user);
            } else {
                throw new ServiceLayerException("User already exists with that email.");
            }
        } else {
            throw new ServiceLayerException("User already exists with that alias.");
        }

        UserProfile userProfile = MappingUtils.convertToType(createdUser, UserProfile.class);

        return new TokenResponse(
                userProfile,
                jwtUtility.generateToken(createdUser.getAlias(), createdUser.getEmail())
        );
    }

}