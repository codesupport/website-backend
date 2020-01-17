package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.exception.ConfigurationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

public class JwtPropertiesTest {

    @Test
    public void shouldCorrectlyParseExpirationSeconds() {
        JwtProperties jwtPropertiesSpy = spy(new JwtProperties());

        jwtPropertiesSpy.setExpiration("5s");

        Long expected = 5 * 1000L;
        Long actual = jwtPropertiesSpy.getExpiration();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseExpirationMinutes() {
        JwtProperties jwtPropertiesSpy = spy(new JwtProperties());

        jwtPropertiesSpy.setExpiration("5m");

        Long expected = 5 * 60 * 1000L;
        Long actual = jwtPropertiesSpy.getExpiration();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCorrectlyParseExpirationHours() {
        JwtProperties jwtPropertiesSpy = spy(new JwtProperties());

        jwtPropertiesSpy.setExpiration("5h");

        Long expected = 5 * 60 * 60 * 1000L;
        Long actual = jwtPropertiesSpy.getExpiration();

        assertEquals(expected, actual);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowConfigurationExceptionForInvalidTimeFormat() {
        JwtProperties jwtProperties = new JwtProperties();

        jwtProperties.setExpiration("-2m");
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowConfigurationExceptionForInvalidUnitFormat() {
        JwtProperties jwtProperties = new JwtProperties();

        jwtProperties.setExpiration("2r");
    }

    @Test
    public void shouldLeaveDefaultTimeIfExpirationEmpty() {
        JwtProperties jwtProperties = new JwtProperties();

        Long expected = jwtProperties.getExpiration();

        jwtProperties.setExpiration(" ");

        Long actual = jwtProperties.getExpiration();

        assertEquals(expected, actual);
    }

}
