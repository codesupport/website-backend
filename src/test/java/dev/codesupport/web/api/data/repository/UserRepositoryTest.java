package dev.codesupport.web.api.data.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    //Unused - Assigned by autowire
    @SuppressWarnings("unused")
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldReturnTrueForExistsByUsernameIfValidUser() {
        assertTrue(userRepository.existsByUsername("Iffy"));
    }

    @Test
    public void shouldReturnFalseForExistsByUsernameIfValidUser() {
        assertFalse(userRepository.existsByUsername("Spiffy"));
    }

}
