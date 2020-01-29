package dev.codesupport.web.common.security;

import dev.codesupport.web.common.security.models.UserDetails;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class EmailPasswordAuthenticationTokenTest {

    @Test
    public void shouldCreateUsernamePasswordAuthenticationTokenBase() {
        Object principal = "principal";
        Object credentials = "credentials";

        EmailPasswordAuthenticationToken actual = new EmailPasswordAuthenticationToken(
                principal,
                credentials
        );

        UsernamePasswordAuthenticationToken expected = new UsernamePasswordAuthenticationToken(
                principal,
                credentials
        );

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCreateUsernamePasswordAuthenticationTokenBaseWithAuthorities() {
        Object principal = "principal";
        Object credentials = "credentials";

        EmailPasswordAuthenticationToken actual = new EmailPasswordAuthenticationToken(
                principal,
                credentials,
                Collections.emptyList()
        );

        UsernamePasswordAuthenticationToken expected = new UsernamePasswordAuthenticationToken(
                principal,
                credentials,
                Collections.emptyList()
        );

        assertEquals(expected, actual);
    }

    @Test
    public void shouldGetCorrectNameIfUserDetailsSet() {
        String email = "email";

        Object principal = new UserDetails(
                "alias",
                "password",
                email,
                Collections.emptySet(),
                false
        );

        Object credentials = "credentials";

        EmailPasswordAuthenticationToken token = new EmailPasswordAuthenticationToken(
                principal,
                credentials,
                Collections.emptyList()
        );

        assertEquals(email, token.getName());
    }

    @Test
    public void shouldGetCorrectNameIfUserDetailsNotSet() {
        Object principal = "principal";
        Object credentials = "credentials";

        EmailPasswordAuthenticationToken token = new EmailPasswordAuthenticationToken(
                principal,
                credentials,
                Collections.emptyList()
        );

        assertEquals(principal, token.getName());
    }

}
