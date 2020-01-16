package dev.codesupport.web.common.security;

import dev.codesupport.web.common.security.models.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface AuthorizationService {

    String createTokenForEmailAndPassword(String email, String password);

    UserDetails getUserDetailsByEmail(String email);

}
