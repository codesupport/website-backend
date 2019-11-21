package dev.codesupport.web.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");

    private ValidationUtils() {

    }

    public static boolean isValidUsername(String username) {
        return !StringUtils.isEmpty(username) &&
                username.length() > 4 &&
                username.length() < 15 &&
                StringUtils.isAlpha(username.substring(0, 1)) &&
                StringUtils.isAlphanumeric(username);
    }

    public static boolean isValidPassword(String password) {
        return password != null &&
                password.length() > 10 &&
                StringUtils.isAlphanumeric(password);
    }

    public static boolean isValidEmail(String email) {
        return !StringUtils.isEmpty(email) &&
                emailPattern.matcher(email).matches();
    }

}
