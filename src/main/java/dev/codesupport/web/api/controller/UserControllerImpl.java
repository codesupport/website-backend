package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.UserService;
import dev.codesupport.web.domain.Permission;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserPasswordChange;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

/**
 * API Contract implementation for the {@link User} resource.
 */
@Component
@Validated
public class UserControllerImpl implements UserController {

    private final UserService service;

    @Autowired
    public UserControllerImpl(UserService userService) {
        service = userService;
    }

    @Override
    public List<User> getAllUsers() {
        return service.findAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        return service.getUserById(id);
    }

    @Override
    public User getCurrentUser() {
        return service.getCurrentUser(new User());
    }

    @Override
    public Set<Permission> getUserPermissions(Long id) {
        return service.getUserPermissionsById(id);
    }

    /**
     * Registers a user, creating a user entry and returning the user's profile
     *
     * @param userRegistration The user registration object with appropriate information.
     * @return The profile for the created user.
     */
    @Override
    public UserProfile registerUser(UserRegistration userRegistration) {
        return service.registerUser(userRegistration);
    }

    @Override
    public UserProfile updatePassword(UserPasswordChange userPasswordChange) {
        return service.updatePassword(userPasswordChange);
    }
}
