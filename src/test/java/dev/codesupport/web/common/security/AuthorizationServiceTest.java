package dev.codesupport.web.common.security;

import dev.codesupport.web.api.ApiServiceApplication;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiServiceApplication.class)
public class AuthorizationServiceTest {

    //unused - This is autowired, IDE can't see for some reason
    @SuppressWarnings("unused")
    @Autowired
    private AuthorizationService authorizationService;

    @After
    public void tearDown() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test(expected = AccessDeniedException.class)
    public void shouldRejectAccessWithUnpermittedUserAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(
                new AnonymousAuthenticationToken(
                        "user",
                        "principle",
                        Collections.singletonList(new SimpleGrantedAuthority("ANONYMOUS_USER"))
                )
        );

        authorizationService.linkDiscord("code");
    }

}
