package dev.codesupport.web.common.util;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ValidationUtilsTest {

    @Test
    public void shouldReturnFalseForUsernameNull() {
        assertFalse(ValidationUtils.isValidUsername(null));
    }

    @Test
    public void shouldReturnFalseForUsernameShorterThanFiveCharacters() {
        String username = "abc";

        assertFalse(ValidationUtils.isValidUsername(username));
    }

    @Test
    public void shouldReturnFalseForUsernameLongerThanFourteenCharacters() {
        String username = "abcdefghijklmno";

        assertFalse(ValidationUtils.isValidUsername(username));
    }

    @Test
    public void shouldReturnFalseForUsernameStartingWithNonAlpha() {
        String username = "1abcdefg";

        assertFalse(ValidationUtils.isValidUsername(username));
    }

    @Test
    public void shouldReturnFalseForUsernameNotAlphanumeric() {
        String username = "abcd1234-";

        assertFalse(ValidationUtils.isValidUsername(username));
    }

    @Test
    public void shouldReturnTrueForValidUsername() {
        String username = "abcdef1";

        assertTrue(ValidationUtils.isValidUsername(username));
    }

    @Test
    public void shouldReturnFalseForPasswordNull() {
        assertFalse(ValidationUtils.isValidPassword(null));
    }

    @Test
    public void shouldReturnFalseForPasswordShorterThanEleven() {
        String validationString = "1234567890";

        assertFalse(ValidationUtils.isValidPassword(validationString));
    }

    @Test
    public void shouldReturnFalseForPasswordWithNonAlphanumeric() {
        String validationString = "12345abcdefg&*";

        assertFalse(ValidationUtils.isValidPassword(validationString));
    }

    @Test
    public void shouldReturnTrueForValidPassword() {
        String validationString = "1234567890abcdefghijk";

        assertTrue(ValidationUtils.isValidPassword(validationString));
    }

    @Test
    public void shouldReturnFalseForNullEmail() {
        assertFalse(ValidationUtils.isValidEmail(null));
    }

    @Test
    public void shouldReturnFalseForBlankEmail() {
        String email = "";

        assertFalse(ValidationUtils.isValidEmail(email));
    }

    @Test
    public void shouldReturnFalseForInvalidEmail() {
        String email = "123@com";

        assertFalse(ValidationUtils.isValidEmail(email));
    }

    @Test
    public void shouldReturnTrueForValidEmail() {
        String email = "123@123.com";

        assertTrue(ValidationUtils.isValidEmail(email));
    }
}
