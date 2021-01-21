package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.api.data.repository.ShowcaseRepository;
import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Permission;
import dev.codesupport.web.domain.Showcase;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Determines access for deleting Showcases
 */
@Component
public class ShowcaseDeleteEvaluator extends AbstractAccessEvaluator<Showcase> {

    private final ShowcaseRepository showcaseRepository;

    public ShowcaseDeleteEvaluator(ShowcaseRepository showcaseRepository) {
        super(Permission.DELETE);
        this.showcaseRepository = showcaseRepository;
    }

    /**
     * Checks if user has right to delete a showcase
     * <p>Only authenticated users (with valid token) that are the original showcase author are allowed to delete
     * the requested showcase.</p>
     *
     * @param auth     The Authentication associated with the access evaluation
     * @param showcase The object associated with the access evaluation
     * @return True if user is authenticated and the original author, False otherwise.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, Showcase showcase) {
        ShowcaseUpdateEvaluator updateEvaluator = new ShowcaseUpdateEvaluator(showcaseRepository);
        return updateEvaluator.hasPermissionCheck(auth, showcase);
    }

}
