package dev.codesupport.web.common.security;

import dev.codesupport.web.common.security.models.UserDetails;
import dev.codesupport.web.domain.TokenResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public interface AuthorizationService {

    TokenResponse createTokenForEmailAndPassword(String email, String password);

    @PreAuthorize("hasPermission('token', 'update')")
    TokenResponse refreshToken();

    UserDetails getUserDetailsByEmail(String email);

    @PreAuthorize("hasPermission('discord', 'link')")
    void linkDiscord(String code);

}
