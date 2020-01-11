package dev.codesupport.web.api.controller;

import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Defines endpoints and validations for the associated API Contract for the {@link User} resource.
 */
@RestController
@RequestMapping("api/user/v1")
@Validated
public interface UserProfileController {

    @GetMapping("/profiles")
    RestResponse<UserProfile> getAllUserProfiles();

    @GetMapping("/profiles/{id}")
    RestResponse<UserProfile> getUserProfileById(@PathVariable Long id);

}
