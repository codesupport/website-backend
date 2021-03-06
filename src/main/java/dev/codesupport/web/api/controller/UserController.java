package dev.codesupport.web.api.controller;

import dev.codesupport.web.domain.Permission;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserPasswordChange;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserRegistration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * Defines endpoints and validations for the associated API Contract for the {@link User} resource.
 */
@RestController
@RequestMapping("/user/v1")
@Api(value = "User", description = "REST API for User", tags = {"User"})
@Validated
public interface UserController {

    @ApiOperation("Get all Users")
    @GetMapping("/users")
    List<User> getAllUsers();

    @ApiOperation("Get User by id")
    @GetMapping("/users/{id}")
    User getUserById(@PathVariable Long id);

    @ApiOperation("Get current User")
    @GetMapping("/current")
    User getCurrentUser();

    @ApiOperation("Get User permissions by id")
    @GetMapping("/permissions/{id}")
    Set<Permission> getUserPermissions(@PathVariable Long id);

    @ApiOperation("Register User")
    @PostMapping("/users")
    UserProfile registerUser(@RequestBody @Valid UserRegistration userRegistration);

    @ApiOperation("Reset Password")
    @PutMapping("/password")
    UserProfile updatePassword(@RequestBody @Valid UserPasswordChange userPasswordChange);

}
