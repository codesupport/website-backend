package dev.codesupport.web.api.service;

import dev.codesupport.web.domain.TokenResponse;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserProfileStripped;
import dev.codesupport.web.domain.UserRegistration;
import dev.codesupport.web.domain.UserStripped;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Interface layer for defining access authorizations, etc.
 */
@Service
public interface UserService {

    @PostAuthorize("hasPermission(returnObject, 'read')")
    UserProfile getUserProfileByAlias(String alias);

    UserProfileStripped getUserProfileById(Long id);

    List<UserProfileStripped> findAllUserProfiles();

    UserStripped getUserById(Long id);

    List<UserStripped> findAllUsers();

    TokenResponse registerUser(UserRegistration userRegistration);

}