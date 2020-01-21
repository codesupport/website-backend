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

    @Override
    protected boolean hasPermissionCheck(Authentication auth, String targetDomainObject, String permission) {
        // If you are a valid authenticated user, you can link your account.
        return isValidAuth(auth);
    }

    @Override
    public Accessor getAccessor() {
        // Set accessor since this evaluation has no associated class type.
        return Accessor.DISCORD;
    }

}
