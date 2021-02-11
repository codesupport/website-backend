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
public class ArticlesReadEvaluator extends AbstractAccessEvaluator<Boolean> {

    public ArticlesReadEvaluator() {
        super(Permission.READ);
    }

    /**
     * Checks if user has right to see article lists
     * <p>Stubbed permission logic, only admins can see non-published articles.</p>
     *
     * @param auth          The Authentication associated with the access evaluation
     * @param publishedOnly Indicates if only published articles are requested
     * @return True if user is allowed to see articles, False otherwise.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, Boolean publishedOnly) {
        boolean hasPermission = Boolean.TRUE.equals(publishedOnly);

        if (!hasPermission) {
            hasPermission = hasPrivilege(auth, "UPDATE_ARTICLE");
        }

        return hasPermission;
    }

    @Override
    public Accessor getAccessor() {
        return Accessor.ARTICLES;
    }
}
