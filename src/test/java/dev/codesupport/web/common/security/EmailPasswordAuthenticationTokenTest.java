package dev.codesupport.web.common.security;

import dev.codesupport.web.common.security.models.UserDetails;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class EmailPasswordAuthenticationTokenTest {

    @Test
    public void shouldGetCorrectNameIfUserDetailsSet() {
        String email = "email";

        UserDetails principal = new UserDetails(
                15L,
                "alias",
                "password",
                email,
                Collections.emptySet(),
                false,
                true
        );

        Object credentials = "credentials";

        EmailPasswordAuthenticationToken token = new EmailPasswordAuthenticationToken(
                principal,
                credentials,
                Collections.emptyList()
        );

        assertEquals(email, token.getName());
    }

}
