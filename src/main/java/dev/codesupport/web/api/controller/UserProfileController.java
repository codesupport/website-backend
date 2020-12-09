package dev.codesupport.web.api.controller;

import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.validation.annotation.AliasConstraint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Defines endpoints and validations for the associated API Contract for the {@link User} resource.
 */
@RestController
@RequestMapping("/user/v1")
@Api(tags = {"User"})
@Validated
public interface UserProfileController {

    @ApiOperation("Get all User Profiles")
    @GetMapping("/profiles")
    List<UserProfile> getAllUserProfiles();

    @ApiOperation("Get User Profile by alias")
    @GetMapping(value = "/profiles", params = {"alias"})
    UserProfile getUserProfileByAlias(@RequestParam @AliasConstraint String alias);

    @ApiOperation("Get User Profile by id")
    @GetMapping("/profiles/{id}")
    UserProfile getUserProfileById(@PathVariable Long id);

}
