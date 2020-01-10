package dev.codesupport.web.common.security.hashing;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class SpringHashingUtilityTest {

    @Test
    public void shoudldCorrectlyHashPassword() {
        String password = "hello";
        String passwordHash = "hashhashhash";

        PasswordEncoder mockEncoder = mock(BCryptPasswordEncoder.class);

        doReturn(passwordHash)
                .when(mockEncoder)
                .encode(password);

        SpringHashingUtility hashingUtility = new SpringHashingUtility(mockEncoder);

        String actual = hashingUtility.hashPassword(password);

        assertEquals(passwordHash, actual);
    }

    @Test
    public void shouldReturnFalseIfPasswordMatchesHashedPassword() {
        String password = "hello";
        String hashPassword = "hashhashhash";

        PasswordEncoder mockEncoder = mock(BCryptPasswordEncoder.class);

        doReturn(false)
                .when(mockEncoder)
                .matches(password, hashPassword);

        SpringHashingUtility hashingUtility = new SpringHashingUtility(mockEncoder);

        assertFalse(hashingUtility.verifyPassword(password, hashPassword));
    }

    @Test
    public void shouldReturnTrueIfPasswordMatchesHashedPassword() {
        String password = "hello";
        String hashPassword = "hashhashhash";

        PasswordEncoder mockEncoder = mock(BCryptPasswordEncoder.class);

        doReturn(true)
                .when(mockEncoder)
                .matches(password, hashPassword);

        SpringHashingUtility hashingUtility = new SpringHashingUtility(mockEncoder);

        assertTrue(hashingUtility.verifyPassword(password, hashPassword));
    }

}
