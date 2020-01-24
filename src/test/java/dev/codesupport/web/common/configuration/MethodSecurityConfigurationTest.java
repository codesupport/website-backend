package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.security.access.AccessEvaluatorFactory;
import dev.codesupport.web.common.security.access.AccessPermissionEvaluator;
import org.junit.Test;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class MethodSecurityConfigurationTest {

    @Test
    public void shouldSetCorrectMethodSecurityExpressionHandler() {
        AccessEvaluatorFactory mockEvaluatorFactory = mock(AccessEvaluatorFactory.class);

        MethodSecurityConfiguration configuration = new MethodSecurityConfiguration(mockEvaluatorFactory);

        MethodSecurityExpressionHandler handler = configuration.createExpressionHandler();

        assertTrue(handler instanceof DefaultMethodSecurityExpressionHandler);
    }

    @Test
    public void shouldSetCorrectPermissionEvaluator() {
        AccessEvaluatorFactory mockEvaluatorFactory = mock(AccessEvaluatorFactory.class);

        MethodSecurityConfiguration configuration = new MethodSecurityConfiguration(mockEvaluatorFactory);

        MethodSecurityExpressionHandler handler = configuration.createExpressionHandler();

        Object evaluator = ReflectionTestUtils.getField(handler, "permissionEvaluator");

        assertTrue(evaluator instanceof AccessPermissionEvaluator);
    }

}
