package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Accessor;
import dev.codesupport.web.common.security.access.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Determines access for reading User permissions
 */
@Component
public class PermissionReadEvaluator extends AbstractAccessEvaluator<Long> {

    public PermissionReadEvaluator() {
        super(Permission.READ);
    }

    /**
     * Performs a passive check if the requester can see the user's permission
     * <p>Only logged in users can see permissions for now.</p>
     *
     * @param auth   The Authentication associated with the access evaluation
     * @param userId The id of the user the permissions are for
     * @return True always.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, Long userId) {
        //TODO: This should check if you're an admin or if they are your permissions, but we don't have that set up yet.
        return isValidAuth(auth);
    }

    @Override
    public Accessor getAccessor() {
        return Accessor.PERMISSION;
    }

}
