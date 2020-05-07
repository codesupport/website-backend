package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Permission;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.UserStripped;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Determines access for creating Articles
 */
@Component
public class ArticleCreateEvaluator extends AbstractAccessEvaluator<Article> {

    private final UserRepository userRepository;

    public ArticleCreateEvaluator(UserRepository userRepository) {
        super(Permission.CREATE);
        this.userRepository = userRepository;
    }

    /**
     * Checks if user has right to create an article
     * <p>Only authenticated users (with valid JWT) are allowed to create an article, as this is needed
     * to know who the user is.</p>
     *
     * @param auth    The Authentication associated with the access evaluation
     * @param article The object associated with the access evaluation
     * @return True if user is authenticated, False otherwise.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, Article article) {
        boolean hasPermission = isValidAuth(auth);

        if (hasPermission) {
            // Set creator of showcase to the authenticated user.
            UserEntity userEntity = userRepository.findByEmail(auth.getName());
            UserStripped user = new UserStripped();
            user.setId(userEntity.getId());
            article.setCreatedBy(user);
            article.setUpdatedBy(user);
        }

        return hasPermission;
    }

}
