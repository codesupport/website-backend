package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Permission;
import dev.codesupport.web.domain.Showcase;
import dev.codesupport.web.domain.User;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Determines access for creating Showcases
 */
@Component
@EqualsAndHashCode(callSuper = true)
public class ShowcaseCreateEvaluator extends AbstractAccessEvaluator<Showcase> {

    private final UserRepository userRepository;

    public ShowcaseCreateEvaluator(UserRepository userRepository) {
        super(Permission.CREATE);
        this.userRepository = userRepository;
    }

    /**
     * Checks if user has right to create a showcase
     * <p>Only authenticated users (with valid token) are allowed to create a showcase, as this is needed
     * to know who the user is.</p>
     *
     * @param auth     The Authentication associated with the access evaluation
     * @param showcase The object associated with the access evaluation
     * @return True if user is authenticated, False otherwise.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, Showcase showcase) {
        boolean hasPermission = isValidAuth(auth);

        if (hasPermission) {
            // Set creator of showcase to the authenticated user.
            Optional<UserEntity> optional = userRepository.findByEmailIgnoreCase(auth.getName());
            if (optional.isPresent()) {
                UserEntity userEntity = optional.get();
                User user = new User();
                user.setId(userEntity.getId());
                showcase.setUser(user);
            } else {
                throw new InvalidUserException(InvalidUserException.Reason.MISSING_USER);
            }
        }

        return hasPermission;
    }

}
