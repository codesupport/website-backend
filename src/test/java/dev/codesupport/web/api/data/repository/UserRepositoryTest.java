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
        assertTrue(userRepository.existsByAliasIgnoreCase("iffy"));
    }

    @Test
    public void shouldReturnFalseForExistsByAliasIfValidUser() {
        assertFalse(userRepository.existsByAliasIgnoreCase("Spiffy"));
    }

    @Test
    public void shouldReturnTrueForExistsByEmailIfValidUser() {
        assertTrue(userRepository.existsByEmailIgnoreCase("IF.fy@cs.dev"));
    }

    @Test
    public void shouldReturnFalseForExistsByEmailIfValidUser() {
        assertFalse(userRepository.existsByEmailIgnoreCase("no@email.com"));
    }

    @Test
    public void shouldReturnCorrectUserByAlias() {
        UserEntity userEntity = userRepository.findByAliasIgnoreCase("iffy");

        String expected = "UserEntity(id=3, alias=Iffy, hashPassword=$2a$10$KuNmt9tVAOvzvcjsiTFzFudhC9bpJbhJfLKiVwwRYCaAPR2LXxJKS, discordId=null, discordUsername=Iffy#<3<3, githubUsername=, jobTitle=null, jobCompany=null, email=if.fy@cs.dev, avatarLink=iffy.jpg, disabled=false, role=RoleEntity(id=1, code=admin, label=admin, permission=[PermissionEntity(id=1, code=write, label=write)]), permission=[], biography=Red sparkles and glitter, country=CountryEntity(id=2, code=uk, label=United Kingdom), userAward=[UserAwardEntity(id=1, code=adv_cd_2019, label=Advent of Code 2019, description=A wonderful description)], joinDate=1570492800000)";
        String actual = userEntity.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectUserByEmail() {
        UserEntity userEntity = userRepository.findByEmailIgnoreCase("IF.fy@cs.dev");

        String expected = "UserEntity(id=3, alias=Iffy, hashPassword=$2a$10$KuNmt9tVAOvzvcjsiTFzFudhC9bpJbhJfLKiVwwRYCaAPR2LXxJKS, discordId=null, discordUsername=Iffy#<3<3, githubUsername=, jobTitle=null, jobCompany=null, email=if.fy@cs.dev, avatarLink=iffy.jpg, disabled=false, role=RoleEntity(id=1, code=admin, label=admin, permission=[PermissionEntity(id=1, code=write, label=write)]), permission=[], biography=Red sparkles and glitter, country=CountryEntity(id=2, code=uk, label=United Kingdom), userAward=[UserAwardEntity(id=1, code=adv_cd_2019, label=Advent of Code 2019, description=A wonderful description)], joinDate=1570492800000)";
        String actual = userEntity.toString();

        assertEquals(expected, actual);
    }

}
