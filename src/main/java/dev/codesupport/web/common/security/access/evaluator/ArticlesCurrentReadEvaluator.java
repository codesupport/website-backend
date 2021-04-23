package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Accessor;
import dev.codesupport.web.common.security.access.Permission;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Determines access for viewing Articles
 */
@Component
@EqualsAndHashCode(callSuper = true)
public class ArticlesCurrentReadEvaluator extends AbstractAccessEvaluator<Long> {

    public ArticlesCurrentReadEvaluator() {
        super(Permission.READ);
    }

    /**
     * Checks if user has right to see article lists
     * <p>Stubbed permission logic, only admins can see non-published articles.</p>
     *
     * @param auth          The Authentication associated with the access evaluation
     * @param creatorId     Filters article list by creator
     * @return True if user is allowed to see articles, False otherwise.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, Long creatorId) {
        return getUserDetails(auth).getId().equals(creatorId);
    }

    @Override
    public Accessor getAccessor() {
        return Accessor.ARTICLES_CURRENT;
    }
}
