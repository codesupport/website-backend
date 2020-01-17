package dev.codesupport.web.common.security.jwt;

import dev.codesupport.web.common.configuration.JwtProperties;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class JwtUtilityTest {

    @Test
    public void shouldReturnCorrectJwtConfiguration() {
        JwtProperties mockJwtProperties = mock(JwtProperties.class);
        JwtUtility jwtUtility = new JwtUtility(mockJwtProperties) {
            @Override
            public String generateToken(String username, String email) {
                return null;
            }

            @Override
            DecodedJWT decode(String tokenString) {
                return null;
            }
        };

        JwtProperties actual = jwtUtility.configuration();

        assertSame(mockJwtProperties, actual);
    }

    @Test
    public void shouldCalculateCorrectExpirationTime() {
        long msToExpire = 5000L;
        JwtProperties mockJwtProperties = mock(JwtProperties.class);
        JwtUtility jwtUtility = new JwtUtility(mockJwtProperties) {
            @Override
            public String generateToken(String username, String email) {
                return null;
            }

            @Override
            DecodedJWT decode(String tokenString) {
                return null;
            }
        };

        Long expected = System.currentTimeMillis() + msToExpire;
        Long actual = jwtUtility.calculateExpiration(msToExpire);

        long delta = actual - expected;

        //TODO: Need to mock the return of currentTimeMillis() for an accurate test
        //This works as an interim test.
        assertTrue(delta < 1000);
    }

}
