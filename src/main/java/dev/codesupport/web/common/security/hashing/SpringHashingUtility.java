package dev.codesupport.web.common.security.hashing;

import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Spring implementation for the {@link HashingUtility}
 *
 * <p>Uses basic spring mechanics to perform required password hashing operations</p>
 */
@Component
@EqualsAndHashCode(callSuper = true)
public class SpringHashingUtility extends HashingUtility {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SpringHashingUtility(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String hashPassword) {
        return passwordEncoder.matches(rawPassword, hashPassword);
    }

}
