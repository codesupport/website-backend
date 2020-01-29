package dev.codesupport.web.api.service;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.security.hashing.HashingUtility;
import dev.codesupport.web.common.security.jwt.JwtUtility;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.common.util.MappingUtils;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserProfileStripped;
import dev.codesupport.web.domain.UserRegistration;
import dev.codesupport.web.domain.UserStripped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Handles the business logic for the various resource operations provided by the API contract endpoints.
 */
@Component
public class UserServiceImpl implements UserService {

    private final CrudOperations<UserEntity, Long, User> userCrudOperations;
    private final CrudOperations<UserEntity, Long, UserProfileStripped> userProfileCrudOperations;

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
        UserEntity userEntity = userRepository.findByAlias(alias);

        return MappingUtils.convertToType(userEntity, UserProfile.class);
    }

    @Override
    public List<UserProfileStripped> getUserProfileById(Long id) {
        return userProfileCrudOperations.getById(id);
    }

    @Override
    public List<UserProfileStripped> findAllUserProfiles() {
        return userProfileCrudOperations.getAll();
    }

    @Override
    public List<UserStripped> getUserById(Long id) {
        List<User> users = userCrudOperations.getById(id);

        return MappingUtils.convertToType(users, UserStripped.class);
    }

    @Override
    public List<UserStripped> findAllUsers() {
        List<User> users = userCrudOperations.getAll();

        return MappingUtils.convertToType(users, UserStripped.class);
    }

    @Override
    public List<String> registerUser(UserRegistration userRegistration) {
        User user = MappingUtils.convertToType(userRegistration, User.class);

        user.setHashPassword(
                hashingUtility.hashPassword(userRegistration.getPassword())
        );

        List<User> users = userCrudOperations.createEntity(user);

        final String token = jwtUtility.generateToken(users.get(0).getAlias(), users.get(0).getEmail());

        return Collections.singletonList(token);
    }

}