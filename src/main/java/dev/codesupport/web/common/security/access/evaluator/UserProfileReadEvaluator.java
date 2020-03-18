package dev.codesupport.web.common.security.access.evaluator;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Permission;
import dev.codesupport.web.domain.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Determines access for reading User Profiles
 */
@Component
public class UserProfileReadEvaluator extends AbstractAccessEvaluator<UserProfile> {

    public UserProfileReadEvaluator() {
        super(Permission.READ);
    }

    /**
     * Performs a passive check if the requester can see the user's email
     * <p>Only the requester that owns the user account is allowed to see their email.</p>
     *
     * @param auth        The Authentication associated with the access evaluation
     * @param userProfile The object associated with the access evaluation
     * @return True always.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, UserProfile userProfile) {
        if (isNotAllowedToSeeEmail(auth, userProfile)) {
            userProfile.setEmail(null);
        }

        return true;
    }

    /**
     * Checks if the given Authentication object has permission to see the email of the given UserProfile
     *
     * @param auth        The Authentication associated with the access evaluation
     * @param userProfile The object associated with the access evaluation
     * @return True if Authentication can not see UserProfile email, False otherwise
     */
    @VisibleForTesting
    boolean isNotAllowedToSeeEmail(Authentication auth, UserProfile userProfile) {
        return !isValidAuth(auth) ||
                !StringUtils.equalsIgnoreCase(
                        auth.getName(),
                        userProfile.getEmail()
                );
    }

}
