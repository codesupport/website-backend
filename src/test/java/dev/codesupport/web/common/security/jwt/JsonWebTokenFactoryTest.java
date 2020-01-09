package dev.codesupport.web.common.security.jwt;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class JsonWebTokenFactoryTest {

    @Test
    public void shouldCreateCorrectJsonWebToken() {
        String issuer = "issuer";
        String username = "user";
        String email = "user@user.us";
        long expiration = 0L;

        String token = "token";
        JwtUtility mockJwtUtility = mock(JwtUtility.class);
        JwtUtility.DecodedJWT mockDecodedJWT = mock(JwtUtility.DecodedJWT.class);

        doReturn(issuer)
                .when(mockDecodedJWT)
                .getIssuer();

        doReturn(email)
                .when(mockDecodedJWT)
                .getEmail();

        doReturn(username)
                .when(mockDecodedJWT)
                .getUsername();

        doReturn(expiration)
                .when(mockDecodedJWT)
                .getExpiration();

        doReturn(mockDecodedJWT)
                .when(mockJwtUtility)
                .decode(token);

        JsonWebTokenFactory jsonWebTokenFactory = new JsonWebTokenFactory(mockJwtUtility);

        String expected = "JsonWebToken(username=user, email=user@user.us, expiration=0, issuer=issuer)";
        String actual = jsonWebTokenFactory.createToken(token).toString();

        assertEquals(expected, actual);
    }

}
