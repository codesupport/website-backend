package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.UserService;
import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import static dev.codesupport.web.common.service.service.RestResponse.restResponse;

/**
 * API Contract implementation for the {@link User} resource.
 */
@Component
@Validated
public class UserProfileControllerImpl implements UserProfileController {

    private final UserService service;

    @Autowired
    public UserProfileControllerImpl(UserService userService) {
        service = userService;
    }

    @Override
    public RestResponse<UserProfile> getAllUserProfiles() {
        return restResponse(service.findAllUserProfiles());
    }

    @Override
    public RestResponse<UserProfile> getUserProfileById(Long id) {
        return restResponse(service.getUserProfileById(id));
    }

}