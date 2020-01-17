package dev.codesupport.web.common.security.jwt;

import dev.codesupport.web.common.configuration.JwtProperties;
import dev.codesupport.web.common.exception.InvalidTokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class JJwtUtilityTest {

    @Test
    public void shouldReturnCorrectJWToken() {
        String username = "user";
        String email = "user@user.us";
        String issuer = "issuer";
        long expiration = 0L;
        JwtProperties mockJwtProperties = mock(JwtProperties.class);

        //ResultOfMethodCallIgnored - Not using results, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(expiration)
                .when(mockJwtProperties)
                .getExpiration();

        //ResultOfMethodCallIgnored - Not using results, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(issuer)
                .when(mockJwtProperties)
                .getIssuer();

        JJwtUtility jJwtUtilitySpy = spy(new JJwtUtility(mockJwtProperties));

        doReturn(expiration)
                .when(jJwtUtilitySpy)
                .calculateExpiration(expiration);

        Key key = (Key) ReflectionTestUtils.getField(jJwtUtilitySpy, "KEY");

        String expected = Jwts.builder()
                .setIssuer(issuer)
                .claim("username", username)
                .claim("expiration", expiration)
                .claim("email", email)
                .signWith(key)
                .compact();

        String actual = jJwtUtilitySpy.generateToken(username, email);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldProduceDifferentHashIfUsingDifferentKeyInstance() {
        String username = "user";
        String email = "user@user.us";
        String issuer = "issuer";
        long expiration = 0L;
        JwtProperties mockJwtProperties = mock(JwtProperties.class);

        //ResultOfMethodCallIgnored - Not using results, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(expiration)
                .when(mockJwtProperties)
                .getExpiration();

        //ResultOfMethodCallIgnored - Not using results, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(issuer)
                .when(mockJwtProperties)
                .getIssuer();

        JJwtUtility jJwtUtilitySpy = spy(new JJwtUtility(mockJwtProperties));

        doReturn(expiration)
                .when(jJwtUtilitySpy)
                .calculateExpiration(expiration);

        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String expected = Jwts.builder()
                .setIssuer(issuer)
                .claim("username", username)
                .claim("expiration", expiration)
                .claim("email", email)
                .signWith(key)
                .compact();

        String actual = jJwtUtilitySpy.generateToken(username, email);

        assertNotEquals(expected, actual);
    }

    @Test(expected = InvalidTokenException.class)
    public void shouldProduceInvalidTokenExceptionDueToExpiredJwtException() {
        String username = "user";
        String issuer = "issuer";
        long expiration = 0L;
        JwtProperties mockJwtProperties = mock(JwtProperties.class);

        JJwtUtility jJwtUtilitySpy = spy(new JJwtUtility(mockJwtProperties));

        Key key = (Key) ReflectionTestUtils.getField(jJwtUtilitySpy, "KEY");

        String testToken = Jwts.builder()
                .setIssuer(issuer)
                .claim("username", username)
                .claim("expiration", expiration)
                .signWith(key)
                .compact();

        jJwtUtilitySpy.decode(testToken);
    }

    @Test(expected = InvalidTokenException.class)
    public void shouldProduceInvalidTokenExceptionDueToMalformedJwtException() {
        JwtProperties mockJwtProperties = mock(JwtProperties.class);

        JJwtUtility jJwtUtilitySpy = spy(new JJwtUtility(mockJwtProperties));

        String testToken = "123.123";

        jJwtUtilitySpy.decode(testToken);
    }

    @Test(expected = InvalidTokenException.class)
    public void shouldProduceInvalidTokenExceptionDueToSignatureException() {
        String username = "user";
        String issuer = "issuer";
        long expiration = 0L;
        JwtProperties mockJwtProperties = mock(JwtProperties.class);

        JJwtUtility jJwtUtilitySpy = spy(new JJwtUtility(mockJwtProperties));

        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String testToken = Jwts.builder()
                .setIssuer(issuer)
                .claim("username", username)
                .claim("expiration", expiration)
                .signWith(key)
                .compact();

        jJwtUtilitySpy.decode(testToken);
    }

    @Test(expected = InvalidTokenException.class)
    public void shouldProduceInvalidTokenExceptionDueToIllegalArgumentException() {
        JwtProperties mockJwtProperties = mock(JwtProperties.class);

        JJwtUtility jJwtUtilitySpy = spy(new JJwtUtility(mockJwtProperties));

        jJwtUtilitySpy.decode(null);
    }

}
