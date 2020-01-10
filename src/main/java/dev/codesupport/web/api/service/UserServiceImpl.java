package dev.codesupport.web.api.service;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.security.hashing.HashingUtility;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.common.util.MappingUtils;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserRegistration;
import dev.codesupport.web.domain.UserStripped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handles the business logic for the various resource operations provided by the API contract endpoints.
 */
@Component
public class UserServiceImpl implements UserService {

    private final CrudOperations<UserEntity, Long, User> userCrudOperations;
    private final CrudOperations<UserEntity, Long, UserProfile> userProfileCrudOperations;

    private final HashingUtility hashingUtility;

    @Autowired
    UserServiceImpl(
            UserRepository userRepository,
            HashingUtility hashingUtility
    ) {
        userCrudOperations = new CrudOperations<>(userRepository, UserEntity.class, User.class);
        userProfileCrudOperations = new CrudOperations<>(userRepository, UserEntity.class, UserProfile.class);

        this.hashingUtility = hashingUtility;
    }

    @Override
    public List<UserProfile> getUserProfileById(Long id) {
        return userProfileCrudOperations.getById(id);
    }

    @Override
    public List<UserProfile> findAllUserProfiles() {
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
    public List<UserStripped> registerUser(UserRegistration userRegistration) {
        User user = MappingUtils.convertToType(userRegistration, User.class);

        user.setHashPassword(
                hashingUtility.hashPassword(userRegistration.getPassword())
        );

        List<User> users = userCrudOperations.createEntity(user);

        return MappingUtils.convertToType(users, UserStripped.class);
    }

}