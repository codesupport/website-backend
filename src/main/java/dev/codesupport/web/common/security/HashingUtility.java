package dev.codesupport.web.common.security;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class HashingUtility {

    public abstract String hashPassword(String rawPassword);

    public abstract boolean verifyPassword(String rawPassword, String hashPassword);

}
