package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Accessor;
import dev.codesupport.web.common.security.access.Permission;
import dev.codesupport.web.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Determines access for creating Users
 */
@Component
public class CurrentReadEvaluator extends AbstractAccessEvaluator<User> {

    public CurrentReadEvaluator() {
        super(Permission.READ);
    }

    /**
     * Performs a passive check if the requester is authenticated
     *
     * @param auth The Authentication associated with the access evaluation
     * @param user User object populated with the current user's ID
     * @return True if you are logged in.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, User user) {
        user.setId(getUserDetails(auth).getId());
        return true;
    }

    @Override
    public Accessor getAccessor() {
        return Accessor.CURRENT;
    }
}
