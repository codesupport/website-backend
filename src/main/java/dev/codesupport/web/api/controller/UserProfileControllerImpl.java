package dev.codesupport.web.api.controller;

import dev.codesupport.web.api.service.UserService;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

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
    public List<UserProfile> getAllUserProfiles() {
        return service.findAllUserProfiles();
    }

    @Override
    public UserProfile getUserProfileByAlias(String alias) {
        return service.getUserProfileByAlias(alias);
    }

    @Override
    public UserProfile getUserProfileById(Long id) {
        return service.getUserProfileById(id);
    }

}