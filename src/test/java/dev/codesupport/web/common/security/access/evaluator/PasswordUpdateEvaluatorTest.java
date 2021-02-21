package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.security.access.Accessor;
import dev.codesupport.web.common.security.access.Permission;
import dev.codesupport.web.common.security.models.UserDetails;
import dev.codesupport.web.domain.UserPasswordChange;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PasswordUpdateEvaluatorTest {

    @Test
    public void shouldSetCorrectPermission() {
        UserRepository mockUserRepository = mock(UserRepository.class);
        PasswordUpdateEvaluator evaluator = new PasswordUpdateEvaluator(mockUserRepository);

        Permission actual = (Permission) ReflectionTestUtils.getField(
                evaluator,
                "permission"
        );

        assertEquals(Permission.UPDATE, actual);
    }

    @Test
    public void shouldSetUserRepository() {
        UserRepository expected = mock(UserRepository.class);
        PasswordUpdateEvaluator evaluator = new PasswordUpdateEvaluator(expected);

        Object actual = ReflectionTestUtils.getField(evaluator, "userRepository");

        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnTrueIfValidAuth() {
        Long userId = 5L;

        PasswordUpdateEvaluator evaluatorSpy = spy(new PasswordUpdateEvaluator(null));

        UserPasswordChange mockUserPasswordChange = mock(UserPasswordChange.class);

        Authentication mockAuth = mock(Authentication.class);

        UserDetails mockUserDetails = mock(UserDetails.class);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn(userId)
                .when(mockUserDetails)
                .getId();

        doThrow(AssertionError.class)
                .when(evaluatorSpy)
                .isValidAuth(any());

        doReturn(true)
                .when(evaluatorSpy)
                .isValidAuth(mockAuth);

        doReturn(null)
                .when(evaluatorSpy)
                .getUserDetails(any());

        doReturn(mockUserDetails)
                .when(evaluatorSpy)
                .getUserDetails(mockAuth);

        assertTrue(evaluatorSpy.hasPermissionCheck(mockAuth, mockUserPasswordChange));
    }

    @Test
    public void shouldSetUserIdIfValidAuth() {
        Long userId = 5L;

        PasswordUpdateEvaluator evaluatorSpy = spy(new PasswordUpdateEvaluator(null));

        UserPasswordChange mockUserPasswordChange = mock(UserPasswordChange.class);

        Authentication mockAuth = mock(Authentication.class);

        UserDetails mockUserDetails = mock(UserDetails.class);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn(userId)
                .when(mockUserDetails)
                .getId();

        doThrow(AssertionError.class)
                .when(evaluatorSpy)
                .isValidAuth(any());

        doReturn(true)
                .when(evaluatorSpy)
                .isValidAuth(mockAuth);

        doReturn(null)
                .when(evaluatorSpy)
                .getUserDetails(any());

        doReturn(mockUserDetails)
                .when(evaluatorSpy)
                .getUserDetails(mockAuth);

        evaluatorSpy.hasPermissionCheck(mockAuth, mockUserPasswordChange);

        verify(mockUserPasswordChange, times(1))
                .setId(userId);
    }

    @Test
    public void shouldReturnTrueIfHasPasswordResetTokenTrue() {
        PasswordUpdateEvaluator evaluatorSpy = spy(new PasswordUpdateEvaluator(null));

        UserPasswordChange mockUserPasswordChange = mock(UserPasswordChange.class);

        Authentication mockAuth = mock(Authentication.class);

        doThrow(AssertionError.class)
                .when(evaluatorSpy)
                .isValidAuth(any());

        doReturn(false)
                .when(evaluatorSpy)
                .isValidAuth(mockAuth);

        doThrow(AssertionError.class)
                .when(evaluatorSpy)
                .hasPasswordResetToken(any());

        doReturn(true)
                .when(evaluatorSpy)
                .hasPasswordResetToken(mockUserPasswordChange);

        assertTrue(evaluatorSpy.hasPermissionCheck(mockAuth, mockUserPasswordChange));
    }

    @Test
    public void shouldReturnFalseIfHasPasswordResetTokenFalse() {
        PasswordUpdateEvaluator evaluatorSpy = spy(new PasswordUpdateEvaluator(null));

        UserPasswordChange mockUserPasswordChange = mock(UserPasswordChange.class);

        Authentication mockAuth = mock(Authentication.class);

        doThrow(AssertionError.class)
                .when(evaluatorSpy)
                .isValidAuth(any());

        doReturn(false)
                .when(evaluatorSpy)
                .isValidAuth(mockAuth);

        doThrow(AssertionError.class)
                .when(evaluatorSpy)
                .hasPasswordResetToken(any());

        doReturn(false)
                .when(evaluatorSpy)
                .hasPasswordResetToken(mockUserPasswordChange);

        assertFalse(evaluatorSpy.hasPermissionCheck(mockAuth, mockUserPasswordChange));
    }

    @Test
    public void shouldReturnTrueIfHasValidPasswordResetTokenTrue() {
        PasswordUpdateEvaluator evaluatorSpy = spy(new PasswordUpdateEvaluator(null));

        UserPasswordChange mockUserPasswordChange = mock(UserPasswordChange.class);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn("token")
                .when(mockUserPasswordChange)
                .getResetToken();

        doThrow(AssertionError.class)
                .when(evaluatorSpy)
                .hasValidPasswordResetToken(any());

        doReturn(true)
                .when(evaluatorSpy)
                .hasValidPasswordResetToken(mockUserPasswordChange);

        assertTrue(evaluatorSpy.hasPasswordResetToken(mockUserPasswordChange));
    }

    @Test
    public void shouldReturnFalseIfHasValidPasswordResetTokenFalse() {
        PasswordUpdateEvaluator evaluatorSpy = spy(new PasswordUpdateEvaluator(null));

        UserPasswordChange mockUserPasswordChange = mock(UserPasswordChange.class);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn("token")
                .when(mockUserPasswordChange)
                .getResetToken();

        doThrow(AssertionError.class)
                .when(evaluatorSpy)
                .hasValidPasswordResetToken(any());

        doReturn(false)
                .when(evaluatorSpy)
                .hasValidPasswordResetToken(mockUserPasswordChange);

        assertFalse(evaluatorSpy.hasPasswordResetToken(mockUserPasswordChange));
    }

    @Test
    public void shouldReturnFalseIfPasswordResetTokenNull() {
        PasswordUpdateEvaluator evaluatorSpy = spy(new PasswordUpdateEvaluator(null));

        UserPasswordChange mockUserPasswordChange = mock(UserPasswordChange.class);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn(null)
                .when(mockUserPasswordChange)
                .getResetToken();

        assertFalse(evaluatorSpy.hasPasswordResetToken(mockUserPasswordChange));
    }

    @Test
    public void shouldReturnFalseIfPasswordResetTokenBlank() {
        PasswordUpdateEvaluator evaluatorSpy = spy(new PasswordUpdateEvaluator(null));

        UserPasswordChange mockUserPasswordChange = mock(UserPasswordChange.class);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn("    ")
                .when(mockUserPasswordChange)
                .getResetToken();

        assertFalse(evaluatorSpy.hasPasswordResetToken(mockUserPasswordChange));
    }

    @Test
    public void shouldReturnTrueIfIsValidPasswordResetToken() {
        Long userId = 5L;
        String token = "token";

        UserRepository mockUserRepository = mock(UserRepository.class);
        PasswordUpdateEvaluator evaluatorSpy = spy(new PasswordUpdateEvaluator(null));

        ReflectionTestUtils.setField(evaluatorSpy, "userRepository", mockUserRepository);

        UserPasswordChange mockUserPasswordChange = mock(UserPasswordChange.class);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn(token)
                .when(mockUserPasswordChange)
                .getResetToken();

        UserEntity mockUserEntity = mock(UserEntity.class);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn(userId)
                .when(mockUserEntity)
                .getId();

        doReturn(Optional.of(mockUserEntity))
                .when(mockUserRepository)
                .findByPasswordResetToken(token);

        assertTrue(evaluatorSpy.hasValidPasswordResetToken(mockUserPasswordChange));
    }

    @Test
    public void shouldSetUserIDIfIsValidPasswordResetToken() {
        Long userId = 5L;
        String token = "token";

        UserRepository mockUserRepository = mock(UserRepository.class);
        PasswordUpdateEvaluator evaluatorSpy = spy(new PasswordUpdateEvaluator(null));

        ReflectionTestUtils.setField(evaluatorSpy, "userRepository", mockUserRepository);

        UserPasswordChange mockUserPasswordChange = mock(UserPasswordChange.class);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn(token)
                .when(mockUserPasswordChange)
                .getResetToken();

        UserEntity mockUserEntity = mock(UserEntity.class);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn(userId)
                .when(mockUserEntity)
                .getId();

        doReturn(Optional.of(mockUserEntity))
                .when(mockUserRepository)
                .findByPasswordResetToken(token);

        evaluatorSpy.hasValidPasswordResetToken(mockUserPasswordChange);

        verify(mockUserPasswordChange, times(1))
                .setId(userId);
    }

    @Test
    public void shouldReturnFalseIfIsNotValidPasswordResetToken() {
        String token = "token";

        UserRepository mockUserRepository = mock(UserRepository.class);
        PasswordUpdateEvaluator evaluatorSpy = spy(new PasswordUpdateEvaluator(null));

        ReflectionTestUtils.setField(evaluatorSpy, "userRepository", mockUserRepository);

        UserPasswordChange mockUserPasswordChange = mock(UserPasswordChange.class);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn(token)
                .when(mockUserPasswordChange)
                .getResetToken();

        doReturn(Optional.empty())
                .when(mockUserRepository)
                .findByPasswordResetToken(token);

        assertFalse(evaluatorSpy.hasValidPasswordResetToken(mockUserPasswordChange));
    }

    @Test
    public void shouldNotSetUserIDIfIsNotValidPasswordResetToken() {
        String token = "token";

        UserRepository mockUserRepository = mock(UserRepository.class);
        PasswordUpdateEvaluator evaluatorSpy = spy(new PasswordUpdateEvaluator(null));

        ReflectionTestUtils.setField(evaluatorSpy, "userRepository", mockUserRepository);

        UserPasswordChange mockUserPasswordChange = mock(UserPasswordChange.class);

        //ResultOfMethodCallIgnored - We're creating a mock, not invoking a method
        //noinspection ResultOfMethodCallIgnored
        doReturn(token)
                .when(mockUserPasswordChange)
                .getResetToken();

        doReturn(Optional.empty())
                .when(mockUserRepository)
                .findByPasswordResetToken(token);

        evaluatorSpy.hasValidPasswordResetToken(mockUserPasswordChange);

        verify(mockUserPasswordChange, times(0))
                .setId(anyLong());
    }

    @Test
    public void shouldReturnCorrectAccessor() {
        Accessor expected = Accessor.PASSWORD;
        Accessor actual = new PasswordUpdateEvaluator(null).getAccessor();

        assertEquals(expected, actual);
    }

}
