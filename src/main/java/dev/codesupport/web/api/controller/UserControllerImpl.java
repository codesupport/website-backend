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

    @Override
    public RestResponse<UserStripped> registerUser(UserRegistration userRegistration) {
        return restResponse(service.registerUser(userRegistration));
    }

}
