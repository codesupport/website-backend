package dev.codesupport.web.api.data.repository;

import dev.codesupport.web.api.data.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        assertTrue(userRepository.existsByEmailIgnoreCase("IFfy@cs.dev"));
    }

    @Test
    public void shouldReturnFalseForExistsByEmailIfValidUser() {
        assertFalse(userRepository.existsByEmailIgnoreCase("no@email.com"));
    }

    @Test
    public void shouldReturnCorrectUserByAlias() {
        Optional<UserEntity> optional = userRepository.findByAliasIgnoreCase("iffy");
        UserEntity userEntity;

        if (optional.isPresent()) {
            userEntity = optional.get();
        } else {
            userEntity = null;
            fail("Failed to find expected user.");
        }

        String expected = "UserEntity(id=3, alias=Iffy, hashPassword=$2a$10$OGVw0XltDpCZS949tqDhu.4ShJLI9sNCdmbjlCb3.PEkk.T0csCGi, verifyToken=null, discordId=null, discordUsername=Iffy#<3<3, githubUsername=, jobTitle=null, jobCompany=null, accessToken=null, accessTokenExpireOn=null, email=iffy@cs.dev, avatarLink=iffy.jpg, disabled=false, role=RoleEntity(id=1, code=admin, label=admin, permission=[PermissionEntity(id=1, code=UPDATE_ARTICLE, label=Update Article), PermissionEntity(id=2, code=read, label=read)]), permission=[PermissionEntity(id=1, code=UPDATE_ARTICLE, label=Update Article)], biography=Red sparkles and glitter, country=CountryEntity(id=2, code=uk, label=United Kingdom), userAward=[UserAwardEntity(id=1, code=adv_cd_2019, label=Advent of Code 2019, description=A wonderful description)], joinDate=1570492800000)";
        String actual = userEntity.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectUserByEmail() {
        Optional<UserEntity> optional = userRepository.findByEmailIgnoreCase("IFfy@cs.dev");
        UserEntity userEntity;

        if (optional.isPresent()) {
            userEntity = optional.get();
        } else {
            userEntity = null;
            fail("Failed to find expected user.");
        }

        String expected = "UserEntity(id=3, alias=Iffy, hashPassword=$2a$10$OGVw0XltDpCZS949tqDhu.4ShJLI9sNCdmbjlCb3.PEkk.T0csCGi, verifyToken=null, discordId=null, discordUsername=Iffy#<3<3, githubUsername=, jobTitle=null, jobCompany=null, accessToken=null, accessTokenExpireOn=null, email=iffy@cs.dev, avatarLink=iffy.jpg, disabled=false, role=RoleEntity(id=1, code=admin, label=admin, permission=[PermissionEntity(id=1, code=UPDATE_ARTICLE, label=Update Article), PermissionEntity(id=2, code=read, label=read)]), permission=[PermissionEntity(id=1, code=UPDATE_ARTICLE, label=Update Article)], biography=Red sparkles and glitter, country=CountryEntity(id=2, code=uk, label=United Kingdom), userAward=[UserAwardEntity(id=1, code=adv_cd_2019, label=Advent of Code 2019, description=A wonderful description)], joinDate=1570492800000)";
        String actual = userEntity.toString();

        assertEquals(expected, actual);
    }

}
