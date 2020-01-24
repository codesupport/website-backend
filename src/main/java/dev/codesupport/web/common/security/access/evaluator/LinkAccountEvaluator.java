package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Accessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Determines access for linking discord account
 */
@Component
public class LinkAccountEvaluator extends AbstractAccessEvaluator<String> {

    /**
     * Checks if user has right to link an account
     * <p>Only authenticated users (with valid JWT) are allowed to link their account, as this is needed
     * to know who the user is.</p>
     *
     * @param auth               The Authentication associated with the access evaluation
     * @param targetDomainObject The object associated with the access evaluation
     * @param permission         The permission associated with the access evaluation
     * @return True if user is authenticated, False otherwise.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, String targetDomainObject, String permission) {
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
        return Accessor.DISCORD;
    }

}
