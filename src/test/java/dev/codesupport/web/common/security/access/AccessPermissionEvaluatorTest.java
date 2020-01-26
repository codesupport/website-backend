package dev.codesupport.web.common.security.access;

import org.junit.Test;
import org.springframework.security.core.Authentication;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class AccessPermissionEvaluatorTest {

    @Test
    public void shouldCorrectlyGetAndInvokeEvaluatorByClassObject() {
        String myObject = "my object";
        String permission = "permission";

        Authentication mockAuthentication = mock(Authentication.class);

        AccessEvaluatorFactory mockFactory = mock(AccessEvaluatorFactory.class);

        AbstractAccessEvaluator<?> mockAccessEvaluator = mock(AbstractAccessEvaluator.class);

        doReturn(true)
                .when(mockAccessEvaluator)
                .hasPermission(mockAuthentication, myObject, permission);

        doReturn(mockAccessEvaluator)
                .when(mockFactory)
                .getEvaluator(myObject);

        AccessPermissionEvaluator permissionEvaluator = new AccessPermissionEvaluator(mockFactory);

        assertTrue(
                permissionEvaluator.hasPermission(mockAuthentication, myObject, permission)
        );
    }

    @Test
    public void shouldCorrectlyGetAndInvokeEvaluatorByClassType() {
        String targetId = "my object";
        String targetType = "type";
        String permission = "permission";

        Authentication mockAuthentication = mock(Authentication.class);

        AccessEvaluatorFactory mockFactory = mock(AccessEvaluatorFactory.class);

        AbstractAccessEvaluator<?> mockAccessEvaluator = mock(AbstractAccessEvaluator.class);

        doReturn(true)
                .when(mockAccessEvaluator)
                .hasPermission(mockAuthentication, targetId, permission);

        doReturn(mockAccessEvaluator)
                .when(mockFactory)
                .getEvaluator(targetType);

        AccessPermissionEvaluator permissionEvaluator = new AccessPermissionEvaluator(mockFactory);

        assertTrue(
                permissionEvaluator.hasPermission(mockAuthentication, targetId, targetType, permission)
        );
    }

}
