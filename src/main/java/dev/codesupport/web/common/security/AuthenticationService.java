package dev.codesupport.web.common.security;

import dev.codesupport.web.api.data.entity.UserEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AuthenticationService {

    String authenticate(String email, String password);

    Optional<UserEntity> getUserByToken(String token);

    String createTokenCookieForUser(UserEntity userEntity);

    @PreAuthorize("hasPermission('discord', 'link')")
    void linkDiscord(String code);

}
