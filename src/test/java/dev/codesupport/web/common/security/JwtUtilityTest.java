package dev.codesupport.web.common.security;

import dev.codesupport.web.common.configuration.JwtConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class JwtUtilityTest {

    @Test
    public void shouldReturnCorrectJwtConfiguration() {
        JwtConfiguration mockJwtConfiguration = mock(JwtConfiguration.class);
        JwtUtility jwtUtility = new JwtUtility(mockJwtConfiguration) {
            @Override
            public String generateToken(String username) {
                return null;
            }

            @Override
            DecodedJWT decode(String tokenString) {
                return null;
            }
        };

        JwtConfiguration actual = jwtUtility.configuration();

        assertSame(mockJwtConfiguration, actual);
    }

    @Test
    public void shouldCalculateCorrectExpirationTime() {
        long msToExpire = 5000L;
        JwtConfiguration mockJwtConfiguration = mock(JwtConfiguration.class);
        JwtUtility jwtUtility = new JwtUtility(mockJwtConfiguration) {
            @Override
            public String generateToken(String username) {
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
