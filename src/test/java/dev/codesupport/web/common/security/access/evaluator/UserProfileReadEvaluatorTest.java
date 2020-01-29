package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.common.security.access.Permission;
import dev.codesupport.web.domain.UserProfile;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class UserProfileReadEvaluatorTest {

    @Test
    public void shouldSetPermissionToRead() {
        UserProfileReadEvaluator evaluator = new UserProfileReadEvaluator();

        Permission actual = (Permission) ReflectionTestUtils.getField(
                evaluator,
                "permission"
        );

        assertEquals(Permission.READ, actual);
    }

    @Test
    public void shouldDisplayEmailIfAllowed() {
        String user = "User";
        String email = "my@email.com";

        UserProfile actual = new UserProfile();
        actual.setAlias(user);
        actual.setEmail(email);

        Authentication mockAuthentication = mock(Authentication.class);
        UserProfileReadEvaluator evaluatorSpy = spy(new UserProfileReadEvaluator());

        doReturn(false)
                .when(evaluatorSpy)
                .isNotAllowedToSeeEmail(
                        mockAuthentication,
                        actual
                );

        UserProfile expected = new UserProfile();
        expected.setAlias(user);
        expected.setEmail(email);

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
