package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.ArticleRepository;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Accessor;
import dev.codesupport.web.common.security.access.Permission;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Determines access for viewing Article revisions
 */
@Component
@EqualsAndHashCode(callSuper = true)
public class ArticleRevisionsReadEvaluator extends AbstractAccessEvaluator<Long> {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public ArticleRevisionsReadEvaluator(UserRepository userRepository, ArticleRepository articleRepository) {
        super(Permission.READ);
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    /**
     * Checks if user has right to see article revisions for the given article
     * <p>Stubbed permission logic, only admins and the article author can see the article's revisions.</p>
     *
     * @param auth      The Authentication associated with the access evaluation
     * @param articleId The articleId of the requested revisions
     * @return True if user is allowed to see the revisions, False otherwise.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, Long articleId) {
        boolean hasPermission = isValidAuth(auth);

        if (hasPermission) {
            ArticleEntity articleEntity = articleRepository.getById(articleId);

            // Set creator of showcase to the authenticated user.
            Optional<UserEntity> optional = userRepository.findByEmailIgnoreCase(auth.getName());

            if (optional.isPresent()) {
                UserEntity userEntity = optional.get();
                // If user is author or is admin
                hasPermission = userEntity.getId().equals(articleEntity.getCreatedBy().getId())
                        || hasPrivilege(auth, "UPDATE_ARTICLE");
            } else {
                throw new IllegalStateException("Could not access user's information");
            }
        }

        return hasPermission;
    }

    @Override
    public Accessor getAccessor() {
        return Accessor.ARTICLE_REVISIONS;
    }
}
