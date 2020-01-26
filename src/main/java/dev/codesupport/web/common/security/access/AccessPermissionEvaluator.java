package dev.codesupport.web.common.security.access;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

/**
 * Used to perform access validation through use of @PreAuthorize and @PostAuthorize.
 */
public class AccessPermissionEvaluator implements PermissionEvaluator {

    private final AccessEvaluatorFactory evaluatorFactory;

    public AccessPermissionEvaluator(AccessEvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }

    /**
     * @param auth               The stored authentication from the security context.
     * @param targetDomainObject The object either passed to or returned from the method.
     * @param permission         The related permission involved with the action.
     * @return True if the user has permission to perform the action, False otherwise.
     */
    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        AbstractAccessEvaluator<?> evaluator = evaluatorFactory.getEvaluator(targetDomainObject);

        return evaluator.hasPermission(auth, targetDomainObject, permission.toString());
    }

    /**
     * @param auth       The stored authentication from the security context.
     * @param targetId   ?
     * @param targetType The class type related to the access permission check.
     * @return True if the user has permission to perform the action, False otherwise.
     */
    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        AbstractAccessEvaluator<?> evaluator = evaluatorFactory.getEvaluator(targetType);

        return evaluator.hasPermission(auth, targetId, permission.toString());
    }
}