package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.security.access.AccessEvaluatorFactory;
import dev.codesupport.web.common.security.access.AccessPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Sets the evaluator to use for method access security
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//, proxyTargetClass = true)//jsr250Enabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    private AccessEvaluatorFactory evaluatorFactory;

    @Autowired
    public MethodSecurityConfiguration(AccessEvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }

    /**
     * Creates  MethodSecurityExpressionHandler for access control logic
     *
     * @return The MethodSecurityExpressionHandler used for access logic
     */
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new AccessPermissionEvaluator(evaluatorFactory));
        return expressionHandler;
    }
}
