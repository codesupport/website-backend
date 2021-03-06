package dev.codesupport.web.api.service;

import dev.codesupport.web.api.data.entity.CountryEntity;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.CountryRepository;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.exception.ResourceNotFoundException;
import dev.codesupport.web.common.exception.ServiceLayerException;
import dev.codesupport.web.common.security.hashing.HashingUtility;
import dev.codesupport.web.common.service.service.CrudOperations;
import dev.codesupport.web.common.util.MappingUtils;
import dev.codesupport.web.domain.Country;
import dev.codesupport.web.domain.NewUser;
import dev.codesupport.web.domain.Permission;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserPasswordChange;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Handles the business logic for the various resource operations provided by the API contract endpoints.
 */
@Component
public class UserServiceImpl implements UserService {

    private final CrudOperations<UserEntity, NewUser, Long> userCrudOperations;
    private final CrudOperations<UserEntity, UserProfile, Long> userProfileCrudOperations;

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;

    private final HashingUtility hashingUtility;

    @Autowired
    UserServiceImpl(
            UserRepository userRepository,
            CountryRepository countryRepository,
            HashingUtility hashingUtility
    ) {
        userCrudOperations = new CrudOperations<>(userRepository, UserEntity.class, NewUser.class);
        userProfileCrudOperations = new CrudOperations<>(userRepository, UserEntity.class, UserProfile.class);
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.hashingUtility = hashingUtility;
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
    public UserProfile getUserProfileById(Long id) {
        return userProfileCrudOperations.getById(id);
    }

    @Override
    public List<UserProfile> findAllUserProfiles() {
        return userProfileCrudOperations.getAll();
    }

    @Override
    public User getUserById(Long id) {
        return MappingUtils.convertToType(userCrudOperations.getById(id), User.class);
    }

    @Override
    public User getCurrentUser(User user) {
        return getUserById(user.getId());
    }

    @Override
    public Set<Permission> getUserPermissionsById(Long id) {
        UserEntity entity = userRepository.getById(id);

        return MappingUtils.convertToType(entity.getPermission(), Permission.class);
    }

    @Override
    public List<User> findAllUsers() {
        return MappingUtils.convertToType(userCrudOperations.getAll(), User.class);
    }

    @Override
    public UserProfile registerUser(UserRegistration userRegistration) {
        NewUser user = MappingUtils.convertToType(userRegistration, NewUser.class);
        NewUser createdUser;

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

        return MappingUtils.convertToType(createdUser, UserProfile.class);
    }

    @Override
    public UserProfile updatePassword(UserPasswordChange userPasswordChange) {
        UserEntity userEntity = userRepository.getById(userPasswordChange.getId());
        userEntity.setHashPassword(
                hashingUtility.hashPassword(userPasswordChange.getPassword())
        );
        UserEntity savedUser = userRepository.save(userEntity);

        return MappingUtils.convertToType(savedUser, UserProfile.class);
    }

    @Override
    public Set<Country> findAllCountries() {
        Set<CountryEntity> entities = new HashSet<>(countryRepository.findAll());

        return MappingUtils.convertToType(entities, Country.class);
    }

}