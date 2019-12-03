package dev.codesupport.web.common.configuration;

import org.junit.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FirebaseAuthenticationProviderTest {

    @Test
    public void shouldReturnTrueForSupportsUsernamePasswordAuthenticationToken() {
        FirebaseAuthenticationProvider authenticationProvider = new FirebaseAuthenticationProvider();

        assertTrue(
                authenticationProvider.supports(UsernamePasswordAuthenticationToken.class)
        );
    }

    @Test
    public void shouldReturnFalseForSupportsNonUsernamePasswordAuthenticationToken() {
        FirebaseAuthenticationProvider authenticationProvider = new FirebaseAuthenticationProvider();

        assertFalse(
                authenticationProvider.supports(AbstractAuthenticationToken.class)
        );
    }

    @Test
    public void shouldReturnNullForAuthenticatingInvalidAuthCredentials() {
        FirebaseAuthenticationProvider authenticationProviderSpy = spy(FirebaseAuthenticationProvider.class);

        Authentication mockAuthentication = mock(Authentication.class);

        String username = "testuser";
        Object password = "password123";

        doReturn(username)
                .when(mockAuthentication)
                .getName();

        doReturn(password)
                .when(mockAuthentication)
                .getCredentials();

        doReturn(false)
                .when(authenticationProviderSpy)
                .shouldAuthenticateAgainstThirdPartySystem(username, password.toString());

        assertNull(
                authenticationProviderSpy.authenticate(mockAuthentication)
        );
    }

    @Test
    public void shouldReturnAuthenticationForAuthenticatingValidAuthCredentials() {
        FirebaseAuthenticationProvider authenticationProviderSpy = spy(FirebaseAuthenticationProvider.class);

        Authentication mockAuthentication = mock(Authentication.class);

        String username = "testuser";
        Object password = "password123";

        doReturn(username)
                .when(mockAuthentication)
                .getName();

        doReturn(password)
                .when(mockAuthentication)
                .getCredentials();

        doReturn(true)
                .when(authenticationProviderSpy)
                .shouldAuthenticateAgainstThirdPartySystem(username, password.toString());

        Authentication expected = new UsernamePasswordAuthenticationToken(
                username,
                password,
                new ArrayList<>()
        );
        Authentication actual = authenticationProviderSpy.authenticate(mockAuthentication);

        assertEquals(expected, actual);
    }

}
