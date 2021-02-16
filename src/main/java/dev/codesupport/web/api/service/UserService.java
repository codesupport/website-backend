package dev.codesupport.web.api.service;

import dev.codesupport.web.domain.Country;
import dev.codesupport.web.domain.Permission;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserRegistration;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostAuthorize("hasPermission(returnObject, 'read')")
    UserProfile getUserProfileById(Long id);

    @PostAuthorize("hasPermission(returnObject, 'read')")
    List<UserProfile> findAllUserProfiles();

    User getUserById(Long id);

    @PreAuthorize("hasPermission(#user, 'current', 'read')")
    User getCurrentUser(User user);

    @PostAuthorize("hasPermission(#id, 'permission', 'read')")
    Set<Permission> getUserPermissionsById(Long id);

    List<User> findAllUsers();

    @PreAuthorize("hasPermission('user', 'create')")
    UserProfile registerUser(UserRegistration userRegistration);

    Set<Country> findAllCountries();

}