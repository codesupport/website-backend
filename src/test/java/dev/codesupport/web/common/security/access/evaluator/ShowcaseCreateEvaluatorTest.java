package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.testutils.builders.ShowcaseBuilder;
import dev.codesupport.testutils.builders.UserBuilder;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.security.access.Permission;
import dev.codesupport.web.domain.Showcase;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class ShowcaseCreateEvaluatorTest {

    @Test
    public void shouldSetPermissionToCreate() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        ShowcaseCreateEvaluator evaluator = new ShowcaseCreateEvaluator(mockUserRepository);

        Permission actual = (Permission) ReflectionTestUtils.getField(
                evaluator,
                "permission"
        );

        assertEquals(Permission.CREATE, actual);
    }

    @Test
    public void shouldSetUserRepository() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        ShowcaseCreateEvaluator evaluator = new ShowcaseCreateEvaluator(mockUserRepository);

        UserRepository actual = (UserRepository) ReflectionTestUtils.getField(
                evaluator,
                "userRepository"
        );

        assertSame(mockUserRepository, actual);
    }

    @Test(expected = InvalidUserException.class)
    public void shouldThrowInvalidUserExceptionIfAuthValidButUserDoesNotExist() {
        String email = "user@user.com";

        ShowcaseBuilder showcaseBuilder = ShowcaseBuilder.builder().id(5L);

        UserRepository mockUserRepository = mock(UserRepository.class);

        Authentication mockAuthentication = mock(Authentication.class);

        doReturn(email)
                .when(mockAuthentication)
                .getName();

        Optional<UserEntity> optional = Optional.empty();

        doReturn(optional)
                .when(mockUserRepository)
                .findByEmailIgnoreCase(email);

        ShowcaseCreateEvaluator evaluatorSpy = spy(new ShowcaseCreateEvaluator(mockUserRepository));

        doReturn(true)
                .when(evaluatorSpy)
                .isValidAuth(mockAuthentication);

        Showcase showcase = showcaseBuilder.buildDomain();

        evaluatorSpy.hasPermissionCheck(mockAuthentication, showcase);
    }

    @Test
    public void shouldReturnFalseIfInvalidAuth() {
        ShowcaseBuilder showcaseBuilder = ShowcaseBuilder.builder().id(5L);

        UserRepository mockUserRepository = mock(UserRepository.class);

        Authentication mockAuthentication = mock(Authentication.class);

        ShowcaseCreateEvaluator evaluatorSpy = spy(new ShowcaseCreateEvaluator(mockUserRepository));

        doReturn(false)
                .when(evaluatorSpy)
                .isValidAuth(mockAuthentication);

        Showcase showcase = showcaseBuilder.buildDomain();

        assertFalse(evaluatorSpy.hasPermissionCheck(mockAuthentication, showcase));
    }

    @Test
    public void shouldNotUpdateShowcaseIfInvalidAuth() {
        String email = "user@user.com";

        ShowcaseBuilder showcaseBuilder = ShowcaseBuilder.builder().id(5L);

        UserRepository mockUserRepository = mock(UserRepository.class);

        Authentication mockAuthentication = mock(Authentication.class);

        doReturn(email)
                .when(mockAuthentication)
                .getName();

        UserEntity userEntity = UserBuilder.builder()
                .id(5L)
                .email(mockAuthentication.getName())
                .buildEntity();

        Optional<UserEntity> optional = Optional.of(userEntity);

        doReturn(optional)
                .when(mockUserRepository)
                .findByEmailIgnoreCase(email);

        ShowcaseCreateEvaluator evaluatorSpy = spy(new ShowcaseCreateEvaluator(mockUserRepository));

        doReturn(false)
                .when(evaluatorSpy)
                .isValidAuth(mockAuthentication);

        Showcase expected = showcaseBuilder.buildDomain();

        Showcase actual = showcaseBuilder.buildDomain();

        evaluatorSpy.hasPermissionCheck(mockAuthentication, actual);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnTrueIfValidAuthAndUserExists() {
        String email = "user@user.com";

        ShowcaseBuilder showcaseBuilder = ShowcaseBuilder.builder().id(5L);

        UserRepository mockUserRepository = mock(UserRepository.class);

        Authentication mockAuthentication = mock(Authentication.class);

        doReturn(email)
                .when(mockAuthentication)
                .getName();

        UserEntity userEntity = UserBuilder.builder()
                .id(5L)
                .email(mockAuthentication.getName())
                .buildEntity();

        Optional<UserEntity> optional = Optional.of(userEntity);

        doReturn(optional)
                .when(mockUserRepository)
                .findByEmailIgnoreCase(email);

        ShowcaseCreateEvaluator evaluatorSpy = spy(new ShowcaseCreateEvaluator(mockUserRepository));

        doReturn(true)
                .when(evaluatorSpy)
                .isValidAuth(mockAuthentication);

        Showcase showcase = showcaseBuilder.buildDomain();

        assertTrue(evaluatorSpy.hasPermissionCheck(mockAuthentication, showcase));
    }

    @Test
    public void shouldCorrectlyUpdateShowcaseIfValidAuthAndUserExists() {
        String email = "user@user.com";

        ShowcaseBuilder showcaseBuilder = ShowcaseBuilder.builder().id(5L);

        UserRepository mockUserRepository = mock(UserRepository.class);

        Authentication mockAuthentication = mock(Authentication.class);

        doReturn(email)
                .when(mockAuthentication)
                .getName();

        UserEntity userEntity = UserBuilder.builder()
                .id(5L)
                .email(mockAuthentication.getName())
                .buildEntity();

        Optional<UserEntity> optional = Optional.of(userEntity);

        doReturn(optional)
                .when(mockUserRepository)
                .findByEmailIgnoreCase(email);

        ShowcaseCreateEvaluator evaluatorSpy = spy(new ShowcaseCreateEvaluator(mockUserRepository));

        doReturn(true)
                .when(evaluatorSpy)
                .isValidAuth(mockAuthentication);

        Showcase expected = showcaseBuilder.buildDomain();
        expected.setUser(new User());
        expected.getUser().setId(userEntity.getId());

        Showcase actual = showcaseBuilder.buildDomain();

        evaluatorSpy.hasPermissionCheck(mockAuthentication, actual);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldEraseEmailIfNotAllowed() {
        String user = "User";
        String email = "my@email.com";

        UserProfile actual = new UserProfile();
        actual.setAlias(user);
        actual.setEmail(email);

        Authentication mockAuthentication = mock(Authentication.class);
        UserProfileReadEvaluator evaluatorSpy = spy(new UserProfileReadEvaluator());

        doReturn(true)
                .when(evaluatorSpy)
                .isNotAllowedToSeeEmail(
                        mockAuthentication,
                        actual
                );

        UserProfile expected = new UserProfile();
        expected.setAlias(user);

        evaluatorSpy.hasPermissionCheck(mockAuthentication, actual);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnTrueForIsNotAllowedToSeeEmailIfInvalidAuth() {
        UserProfileReadEvaluator evaluator = new UserProfileReadEvaluator();

        assertTrue(
                evaluator.isNotAllowedToSeeEmail(null, new UserProfile())
        );
    }

    @Test
    public void shouldReturnTrueForIsNotAllowedToSeeEmailIfEmailsDontMatch() {
        Authentication mockAuthentication = mock(Authentication.class);

        doReturn("email1")
                .when(mockAuthentication)
                .getName();

        UserProfile mockUserProfile = mock(UserProfile.class);

        //ResultOfMethodCallIgnored - Not invoking a method, we are creating a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn("email2")
                .when(mockUserProfile)
                .getEmail();

        UserProfileReadEvaluator evaluator = new UserProfileReadEvaluator();

        assertTrue(
                evaluator.isNotAllowedToSeeEmail(mockAuthentication, mockUserProfile)
        );
    }

    @Test
    public void shouldReturnFalseForIsNotAllowedToSeeEmailIfEmailsMatch() {
        String email = "email";

        Authentication mockAuthentication = mock(Authentication.class);

        doReturn(email)
                .when(mockAuthentication)
                .getName();

        UserProfile mockUserProfile = mock(UserProfile.class);

        //ResultOfMethodCallIgnored - Not invoking a method, we are creating a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn(email)
                .when(mockUserProfile)
                .getEmail();

        UserProfileReadEvaluator evaluator = new UserProfileReadEvaluator();

        assertFalse(
                evaluator.isNotAllowedToSeeEmail(mockAuthentication, mockUserProfile)
        );
    }

}
