package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.api.data.entity.ShowcaseEntity;
import dev.codesupport.web.api.data.repository.ShowcaseRepository;
import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Permission;
import dev.codesupport.web.domain.Showcase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Determines access for updating Showcases
 */
@Component
public class ShowcaseUpdateEvaluator extends AbstractAccessEvaluator<Showcase> {

    private final ShowcaseRepository showcaseRepository;

    public ShowcaseUpdateEvaluator(ShowcaseRepository showcaseRepository) {
        super(Permission.UPDATE);
        this.showcaseRepository = showcaseRepository;
    }

    /**
     * Checks if user has right to update a showcase
     * <p>Only authenticated users (with valid token) that are the original showcase author are allowed to update
     * the requested showcase.</p>
     *
     * @param auth     The Authentication associated with the access evaluation
     * @param showcase The object associated with the access evaluation
     * @return True if user is authenticated and the original author, False otherwise.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, Showcase showcase) {
        boolean hasPermission;

        if (isValidAuth(auth)) {
            // Get original showcase
            Optional<ShowcaseEntity> showcaseEntityOptional = showcaseRepository.findById(showcase.getId());
            // Determine if authenticated user is original author
            hasPermission = showcaseEntityOptional.filter(showcaseEntity -> StringUtils.equalsIgnoreCase(
                    showcaseEntity.getUser().getEmail(),
                    auth.getName()
            )).isPresent();
        } else {
            hasPermission = false;
        }

        return hasPermission;
    }

}
