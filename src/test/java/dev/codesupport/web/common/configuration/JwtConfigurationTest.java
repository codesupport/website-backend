package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.exception.ConfigurationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

public class JwtConfigurationTest {

    @Test
    public void shouldCorrectlyParseExpirationSeconds() {
        JwtConfiguration jwtConfigurationSpy = spy(new JwtConfiguration());

        jwtConfigurationSpy.setExpiration("5s");

        Long expected = 5 * 1000L;
        Long actual = jwtConfigurationSpy.getExpiration();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseExpirationMinutes() {
        JwtConfiguration jwtConfigurationSpy = spy(new JwtConfiguration());

        jwtConfigurationSpy.setExpiration("5m");

        Long expected = 5 * 60 * 1000L;
        Long actual = jwtConfigurationSpy.getExpiration();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseExpirationHours() {
        JwtConfiguration jwtConfigurationSpy = spy(new JwtConfiguration());

        jwtConfigurationSpy.setExpiration("5h");

        Long expected = 5 * 60 * 60 * 1000L;
        Long actual = jwtConfigurationSpy.getExpiration();

        assertEquals(expected, actual);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowConfigurationExceptionForInvalidTimeFormat() {
        JwtConfiguration jwtConfiguration = new JwtConfiguration();

        jwtConfiguration.setExpiration("-2m");
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowConfigurationExceptionForInvalidUnitFormat() {
        JwtConfiguration jwtConfiguration = new JwtConfiguration();

        jwtConfiguration.setExpiration("2r");
    }

    @Test
    public void shouldLeaveDefaultTimeIfExpirationEmpty() {
        JwtConfiguration jwtConfiguration = new JwtConfiguration();

        Long expected = jwtConfiguration.getExpiration();

        jwtConfiguration.setExpiration(" ");

        Long actual = jwtConfiguration.getExpiration();

        assertEquals(expected, actual);
    }

}
