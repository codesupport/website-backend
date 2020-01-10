package dev.codesupport.web.common.configuration;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertTrue;

public class EncryptionConfigurationTest {

    @Test
    public void shouldReturnCorrectPasswordEncoderType() {
        EncryptionConfiguration configuration = new EncryptionConfiguration();

        PasswordEncoder actual = configuration.passwordEncoder();

        assertTrue(actual instanceof BCryptPasswordEncoder);
    }

}
