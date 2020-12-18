package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Accessor;
import dev.codesupport.web.common.security.access.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Determines access for creating Users
 */
@Component
public class UserCreateEvaluator extends AbstractAccessEvaluator<String> {

    public UserCreateEvaluator() {
        super(Permission.CREATE);
    }

    /**
     * Performs a passive check if the requester can create users
     * <p>Only logged in users can create other users for now till permissions are sorted</p>
     *
     * @param auth   The Authentication associated with the access evaluation
     * @param unused This property is not used
     * @return True if you are logged in.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, String unused) {
        //TODO: This will go away when permissions are implemented fully.
        return isValidAuth(auth);
    }

    @Override
    public Accessor getAccessor() {
        return Accessor.USER;
    }
}
