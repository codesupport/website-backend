package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.common.security.access.Accessor;
import dev.codesupport.web.common.security.access.Permission;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;

public class TokenRefreshEvaluatorTest {

    @Test
    public void shouldSetPermissionToUpdate() {
        TokenRefreshEvaluator evaluator = new TokenRefreshEvaluator();

        Permission actual = (Permission) ReflectionTestUtils.getField(
                evaluator,
                "permission"
        );

        assertEquals(Permission.UPDATE, actual);
    }

    @Test
    public void shouldGetCorrectAccessor() {
        TokenRefreshEvaluator evaluator = new TokenRefreshEvaluator();

        assertEquals(Accessor.TOKEN, evaluator.getAccessor());
    }

}
