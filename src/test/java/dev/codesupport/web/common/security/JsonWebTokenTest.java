package dev.codesupport.web.common.security;

import dev.codesupport.web.common.exception.InvalidTokenException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class JsonWebTokenTest {

    @Test(expected = InvalidTokenException.class)
    public void shouldThrowInvalidTokenExceptionIfTokenNull() {
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        JwtUtility.DecodedJWT mockDecodedJWT = mock(JwtUtility.DecodedJWT.class);

        doReturn(mockDecodedJWT)
                .when(mockJwtUtility)
                .decode(any());

        new JsonWebToken(mockJwtUtility, null);
    }

    @Test(expected = InvalidTokenException.class)
    public void shouldThrowInvalidTokenExceptionIfTokenEmpty() {
        String token = "";
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        JwtUtility.DecodedJWT mockDecodedJWT = mock(JwtUtility.DecodedJWT.class);

        doReturn(mockDecodedJWT)
                .when(mockJwtUtility)
                .decode(token);

        new JsonWebToken(mockJwtUtility, token);
    }

    @Test(expected = InvalidTokenException.class)
    public void shouldThrowInvalidTokenExceptionIfTokenMissingIssuer() {
        String username = "user";
        long expiration = 0L;

        String token = "token";
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        JwtUtility.DecodedJWT mockDecodedJWT = mock(JwtUtility.DecodedJWT.class);

        doReturn(null)
                .when(mockDecodedJWT)
                .getIssuer();

        doReturn(username)
                .when(mockDecodedJWT)
                .getUsername();

        doReturn(expiration)
                .when(mockDecodedJWT)
                .getExpiration();

        doReturn(mockDecodedJWT)
                .when(mockJwtUtility)
                .decode(token);

        new JsonWebToken(mockJwtUtility, token);
    }

    @Test(expected = InvalidTokenException.class)
    public void shouldThrowInvalidTokenExceptionIfTokenMissingUsername() {
        String issuer = "issuer";
        long expiration = 0L;

        String token = "token";
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        JwtUtility.DecodedJWT mockDecodedJWT = mock(JwtUtility.DecodedJWT.class);

        doReturn(issuer)
                .when(mockDecodedJWT)
                .getIssuer();

        doReturn(null)
                .when(mockDecodedJWT)
                .getUsername();

        doReturn(expiration)
                .when(mockDecodedJWT)
                .getExpiration();

        doReturn(mockDecodedJWT)
                .when(mockJwtUtility)
                .decode(token);

        new JsonWebToken(mockJwtUtility, token);
    }

    @Test(expected = InvalidTokenException.class)
    public void shouldThrowInvalidTokenExceptionIfTokenMissingExpiration() {
        String issuer = "issuer";
        String username = "user";

        String token = "token";
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        JwtUtility.DecodedJWT mockDecodedJWT = mock(JwtUtility.DecodedJWT.class);

        doReturn(issuer)
                .when(mockDecodedJWT)
                .getIssuer();

        doReturn(username)
                .when(mockDecodedJWT)
                .getUsername();

        doReturn(null)
                .when(mockDecodedJWT)
                .getExpiration();

        doReturn(mockDecodedJWT)
                .when(mockJwtUtility)
                .decode(token);

        new JsonWebToken(mockJwtUtility, token);
    }

    @Test
    public void shouldReturnCorrectJsonWebTokenIfValid() {
        String issuer = "issuer";
        String username = "user";
        long expiration = 0L;

        String token = "token";
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        JwtUtility.DecodedJWT mockDecodedJWT = mock(JwtUtility.DecodedJWT.class);

        doReturn(issuer)
                .when(mockDecodedJWT)
                .getIssuer();

        doReturn(username)
                .when(mockDecodedJWT)
                .getUsername();

        doReturn(expiration)
                .when(mockDecodedJWT)
                .getExpiration();

        doReturn(mockDecodedJWT)
                .when(mockJwtUtility)
                .decode(token);

        String expected = "JsonWebToken(username=user, expiration=0, issuer=issuer)";
        String actual = new JsonWebToken(mockJwtUtility, token).toString();

        assertEquals(expected, actual);
    }

}
