package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.api.data.entity.ArticleRevisionEntity;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.ArticleRepository;
import dev.codesupport.web.api.data.repository.ArticleRevisionRepository;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Accessor;
import dev.codesupport.web.common.security.access.Permission;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Determines access for viewing an Article revision
 */
@Component
@EqualsAndHashCode(callSuper = true)
public class ArticleRevisionReadEvaluator extends AbstractAccessEvaluator<Long> {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleRevisionRepository articleRevisionRepository;

    public ArticleRevisionReadEvaluator(
            UserRepository userRepository,
            ArticleRepository articleRepository,
            ArticleRevisionRepository articleRevisionRepository
    ) {
        super(Permission.READ);
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.articleRevisionRepository = articleRevisionRepository;
    }

    /**
     * Checks if user has right to see the article revision
     * <p>Stubbed permission logic, only admins and the article author can see the article revision.</p>
     *
     * @param auth       The Authentication associated with the access evaluation
     * @param revisionId The id of the requested revision
     * @return True if user is allowed to see the revision, False otherwise.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, Long revisionId) {
        boolean hasPermission = isValidAuth(auth);

        if (hasPermission) {
            ArticleRevisionEntity articleRevisionEntity = articleRevisionRepository.getById(revisionId);
            ArticleEntity articleEntity = articleRepository.getById(articleRevisionEntity.getArticleId());

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
        return Accessor.ARTICLE_REVISION;
    }
}
