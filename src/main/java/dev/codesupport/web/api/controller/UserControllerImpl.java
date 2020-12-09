package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.UserService;
import dev.codesupport.web.domain.TokenResponse;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

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

    /**
     * Registers a user, creating a user entry and returning a valid JWT
     *
     * @param userRegistration The user registration object with appriate information.
     * @return A valid JWT for the created user.
     */
    @Override
    public TokenResponse registerUser(UserRegistration userRegistration) {
        return service.registerUser(userRegistration);
    }

}
