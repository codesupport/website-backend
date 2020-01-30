package dev.codesupport.web.common.security.access;

import org.junit.Test;
import org.springframework.security.core.Authentication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class AccessPermissionEvaluatorTest {

    @Test
    public void shouldCorrectlyGetAndInvokeEvaluatorWithFailureByClassObject() {
        String myObject = "my object";
        String permission = "read";

        Authentication mockAuthentication = mock(Authentication.class);

        AccessEvaluatorFactory mockFactory = mock(AccessEvaluatorFactory.class);

        AbstractAccessEvaluator<?> mockAccessEvaluator = mock(AbstractAccessEvaluator.class);

        doReturn(false)
                .when(mockAccessEvaluator)
                .hasPermission(mockAuthentication, myObject);

        doReturn(mockAccessEvaluator)
                .when(mockFactory)
                .getEvaluator(myObject, permission);

        AccessPermissionEvaluator permissionEvaluator = new AccessPermissionEvaluator(mockFactory);

        assertFalse(
                permissionEvaluator.hasPermission(mockAuthentication, myObject, permission)
        );
    }

    @Test
    public void shouldCorrectlyGetAndInvokeEvaluatorWithSuccessIfObjectNullByClassObject() {
        String permission = "read";

        Authentication mockAuthentication = mock(Authentication.class);

        AccessEvaluatorFactory mockFactory = mock(AccessEvaluatorFactory.class);

        AccessPermissionEvaluator permissionEvaluator = new AccessPermissionEvaluator(mockFactory);

        assertTrue(
                permissionEvaluator.hasPermission(mockAuthentication, null, permission)
        );
    }

    @Test
    public void shouldCorrectlyGetAndInvokeEvaluatorByClassObject() {
        String myObject = "my object";
        String permission = "read";

        Authentication mockAuthentication = mock(Authentication.class);

        AccessEvaluatorFactory mockFactory = mock(AccessEvaluatorFactory.class);

        AbstractAccessEvaluator<?> mockAccessEvaluator = mock(AbstractAccessEvaluator.class);

        doReturn(true)
                .when(mockAccessEvaluator)
                .hasPermission(mockAuthentication, myObject);

        doReturn(mockAccessEvaluator)
                .when(mockFactory)
                .getEvaluator(myObject, permission);

        AccessPermissionEvaluator permissionEvaluator = new AccessPermissionEvaluator(mockFactory);

        assertTrue(
                permissionEvaluator.hasPermission(mockAuthentication, myObject, permission)
        );
    }

    @Test
    public void shouldCorrectlyGetAndInvokeEvaluatorWithFailureByClassType() {
        String targetId = "my object";
        String targetType = "type";
        String permission = "READ";

        Authentication mockAuthentication = mock(Authentication.class);

        AccessEvaluatorFactory mockFactory = mock(AccessEvaluatorFactory.class);

        AbstractAccessEvaluator<?> mockAccessEvaluator = mock(AbstractAccessEvaluator.class);

        doReturn(false)
                .when(mockAccessEvaluator)
                .hasPermission(mockAuthentication, targetId);

        doReturn(mockAccessEvaluator)
                .when(mockFactory)
                .getEvaluatorByName(targetType.toLowerCase(), permission.toLowerCase());

        AccessPermissionEvaluator permissionEvaluator = new AccessPermissionEvaluator(mockFactory);

        assertFalse(
                permissionEvaluator.hasPermission(mockAuthentication, targetId, targetType, permission)
        );
    }

    @Test
    public void shouldCorrectlyGetAndInvokeEvaluatorWithSuccessIfObjectNullByClassType() {
        String targetType = "type";
        String permission = "READ";

        Authentication mockAuthentication = mock(Authentication.class);

        AccessEvaluatorFactory mockFactory = mock(AccessEvaluatorFactory.class);

        AccessPermissionEvaluator permissionEvaluator = new AccessPermissionEvaluator(mockFactory);

        assertTrue(
                permissionEvaluator.hasPermission(mockAuthentication, null, targetType, permission)
        );
    }

    @Test
    public void shouldCorrectlyGetAndInvokeEvaluatorByClassType() {
        String targetId = "my object";
        String targetType = "type";
        String permission = "READ";

        Authentication mockAuthentication = mock(Authentication.class);

        AccessEvaluatorFactory mockFactory = mock(AccessEvaluatorFactory.class);

        AbstractAccessEvaluator<?> mockAccessEvaluator = mock(AbstractAccessEvaluator.class);

        doReturn(true)
                .when(mockAccessEvaluator)
                .hasPermission(mockAuthentication, targetId);

        doReturn(mockAccessEvaluator)
                .when(mockFactory)
                .getEvaluatorByName(targetType.toLowerCase(), permission.toLowerCase());

        AccessPermissionEvaluator permissionEvaluator = new AccessPermissionEvaluator(mockFactory);

        assertTrue(
                permissionEvaluator.hasPermission(mockAuthentication, targetId, targetType, permission)
        );
    }

}