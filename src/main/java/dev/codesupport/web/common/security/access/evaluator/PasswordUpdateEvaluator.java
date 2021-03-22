package dev.codesupport.web.common.security.access.evaluator;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Accessor;
import dev.codesupport.web.common.security.access.Permission;
import dev.codesupport.web.common.security.models.UserDetails;
import dev.codesupport.web.domain.UserPasswordChange;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Determines access for updating User password
 */
@Component
@EqualsAndHashCode(callSuper = true)
public class PasswordUpdateEvaluator extends AbstractAccessEvaluator<UserPasswordChange> {

    private final UserRepository userRepository;

    public PasswordUpdateEvaluator(UserRepository userRepository) {
        super(Permission.UPDATE);
        this.userRepository = userRepository;
    }

    /**
     * Performs a passive check if the requester can update their password
     * <p>Only logged in users or requests with valid passwordResetTokens can update their password</p>
     *
     * @param auth               The Authentication associated with the access evaluation
     * @param userPasswordChange The password change request object
     * @return True if you are logged in.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, UserPasswordChange userPasswordChange) {
        boolean hasPermission;

        if (isValidAuth(auth)) {
            hasPermission = true;
            UserDetails userDetails = getUserDetails(auth);
            userPasswordChange.setId(userDetails.getId());
        } else {
            hasPermission = hasPasswordResetToken(userPasswordChange);
        }

        return hasPermission;
    }

    @VisibleForTesting
    boolean hasPasswordResetToken(UserPasswordChange userPasswordChange) {
        boolean hasPermission;

        if (StringUtils.isNotBlank(userPasswordChange.getResetToken())) {
            hasPermission = hasValidPasswordResetToken(userPasswordChange);
        } else {
            hasPermission = false;
        }

        return hasPermission;
    }

    @VisibleForTesting
    boolean hasValidPasswordResetToken(UserPasswordChange userPasswordChange) {
        boolean hasPermission;

        Optional<UserEntity> optional =
                userRepository.findByPasswordResetToken(userPasswordChange.getResetToken());

        if (optional.isPresent()) {
            hasPermission = true;
            userPasswordChange.setId(optional.get().getId());
        } else {
            hasPermission = false;
        }

        return hasPermission;
    }

    @Override
    public Accessor getAccessor() {
        return Accessor.PASSWORD;
    }
}
