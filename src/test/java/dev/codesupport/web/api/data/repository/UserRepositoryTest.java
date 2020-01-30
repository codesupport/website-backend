package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
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
    public void shouldReturnTrueForExistsByAliasIfValidUser() {
        assertTrue(userRepository.existsByAlias("Iffy"));
    }

    @Test
    public void shouldReturnFalseForExistsByAliasIfValidUser() {
        assertFalse(userRepository.existsByAlias("Spiffy"));
    }

    @Test
    public void shouldReturnTrueForExistsByEmailIfValidUser() {
        assertTrue(userRepository.existsByEmail("if.fy@cs.dev"));
    }

    @Test
    public void shouldReturnFalseForExistsByEmailIfValidUser() {
        assertFalse(userRepository.existsByEmail("no@email.com"));
    }

    @Test
    public void shouldReturnCorrectUserByAlias() {
        UserEntity userEntity = userRepository.findByAlias("Iffy");

        String expected = "UserEntity(id=3, alias=Iffy, hashPassword=$2a$10$KuNmt9tVAOvzvcjsiTFzFudhC9bpJbhJfLKiVwwRYCaAPR2LXxJKS, discordId=null, email=if.fy@cs.dev, avatarLink=iffy.jpg, disabled=false, role=RoleEntity(id=1, code=admin, label=admin, permission=[PermissionEntity(id=1, code=write, label=write)]), permission=[], biography=Red sparkles and glitter, gitUrl= , country=CountryEntity(id=2, code=uk, label=United Kingdom), userAward=[UserAwardEntity(id=1, code=adv_cd_2019, label=Advent of Code 2019, description=A wonderful description)], joinDate=1570492800000)";
        String actual = userEntity.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectUserByEmail() {
        UserEntity userEntity = userRepository.findByEmail("if.fy@cs.dev");

        String expected = "UserEntity(id=3, alias=Iffy, hashPassword=$2a$10$KuNmt9tVAOvzvcjsiTFzFudhC9bpJbhJfLKiVwwRYCaAPR2LXxJKS, discordId=null, email=if.fy@cs.dev, avatarLink=iffy.jpg, disabled=false, role=RoleEntity(id=1, code=admin, label=admin, permission=[PermissionEntity(id=1, code=write, label=write)]), permission=[], biography=Red sparkles and glitter, gitUrl= , country=CountryEntity(id=2, code=uk, label=United Kingdom), userAward=[UserAwardEntity(id=1, code=adv_cd_2019, label=Advent of Code 2019, description=A wonderful description)], joinDate=1570492800000)";
        String actual = userEntity.toString();

        assertEquals(expected, actual);
    }

}
