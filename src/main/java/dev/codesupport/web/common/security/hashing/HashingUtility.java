package dev.codesupport.web.common.security.hashing;

import lombok.EqualsAndHashCode;

/**
 * The HashingUtility contract for creating utility implementations.
 */
@EqualsAndHashCode
public abstract class HashingUtility {

    /**
     * Turns a raw clear text password into a hashed string.
     *
     * @param rawPassword The raw password to hash
     * @return The hashed password string
     */
    public abstract String hashPassword(String rawPassword);


    /**
     * Hashes & compares a raw clear text password to a given pre-hashed password to verify a match.
     *
     * @param rawPassword  The raw password to check.
     * @param hashPassword The pre-hashed version of the password to verify against.
     * @return True if the hashed raw password matches the provided hashPassword, False otherwise.
     */
    public abstract boolean verifyPassword(String rawPassword, String hashPassword);

}
