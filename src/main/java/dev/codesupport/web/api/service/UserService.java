package dev.codesupport.web.api.service;

import dev.codesupport.web.domain.Permission;
import dev.codesupport.web.domain.TokenResponse;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserRegistration;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Interface layer for defining access authorizations, etc.
 */
@Service
public interface UserService {

    @PostAuthorize("hasPermission(returnObject, 'read')")
    UserProfile getUserProfileByAlias(String alias);

    UserProfile getUserProfileById(Long id);

    List<UserProfile> findAllUserProfiles();

    User getUserById(Long id);

    Set<Permission> getUserPermissionsById(Long id);

    List<User> findAllUsers();

    TokenResponse registerUser(UserRegistration userRegistration);

}