package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Accessor;
import dev.codesupport.web.common.security.access.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Determines access to refresh JWT
 */
@Component
public class TokenRefreshEvaluator extends AbstractAccessEvaluator<String> {

    public TokenRefreshEvaluator() {
        super(Permission.UPDATE);
    }

    /**
     * Checks if user has right to refresh their token
     * <p>Only authenticated users (with valid JWT) are allowed to refresh their token</p>
     *
     * @param auth               The Authentication associated with the access evaluation
     * @param targetDomainObject The object associated with the access evaluation
     * @return True if user is authenticated, False otherwise.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, String targetDomainObject) {
        // If you are a valid authenticated user, you can link your account.
        return isValidAuth(auth);
    }

    /**
     * There is no class associated with this permission, thus using Accessor enum to identify
     *
     * @return The Accessor associated with this permission evaluation.
     */
    @Override
    public Accessor getAccessor() {
        // Set accessor since this evaluation has no associated class type.
        return Accessor.TOKEN;
    }

}
