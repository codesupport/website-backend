package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.security.CustomPermissionEvaluator;
import org.junit.Test;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertTrue;

public class MethodSecurityConfigurationTest {

    @Test
    public void shouldSetCorrectMethodSecurityExpressionHandler() {
        MethodSecurityConfiguration configuration = new MethodSecurityConfiguration();

        MethodSecurityExpressionHandler handler = configuration.createExpressionHandler();

        assertTrue(handler instanceof DefaultMethodSecurityExpressionHandler);
    }

    @Test
    public void shouldSetCorrectPermissionEvaluator() {
        MethodSecurityConfiguration configuration = new MethodSecurityConfiguration();

        MethodSecurityExpressionHandler handler = configuration.createExpressionHandler();

        Object evaluator = ReflectionTestUtils.getField(handler, "permissionEvaluator");

        assertTrue(evaluator instanceof CustomPermissionEvaluator);
    }

}
