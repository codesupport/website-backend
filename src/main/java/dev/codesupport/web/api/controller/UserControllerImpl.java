package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.UserService;
import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserRegistration;
import dev.codesupport.web.domain.UserStripped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import static dev.codesupport.web.common.service.service.RestResponse.restResponse;

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
    public RestResponse<UserStripped> getAllUsers() {
        return restResponse(service.findAllUsers());
    }

    @Override
    public RestResponse<UserStripped> getUserById(Long id) {
        return restResponse(service.getUserById(id));
    }

    /**
     * Registers a user, creating a user entry and returning a valid JWT
     *
     * @param userRegistration The user registration object with appriate information.
     * @return A valid JWT for the created user.
     */
    @Override
    public RestResponse<String> registerUser(UserRegistration userRegistration) {
        return restResponse(service.registerUser(userRegistration));
    }

}
