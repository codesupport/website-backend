package dev.codesupport.web.api.service;

import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserRegistration;
import dev.codesupport.web.domain.UserStripped;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Interface layer for defining access authorizations, etc.
 */
@Service
public interface UserService {

    List<UserProfile> getUserProfileById(Long id);

    List<UserProfile> findAllUserProfiles();

    List<UserStripped> getUserById(Long id);

    List<UserStripped> findAllUsers();

    List<UserStripped> registerUser(UserRegistration userRegistration);

}