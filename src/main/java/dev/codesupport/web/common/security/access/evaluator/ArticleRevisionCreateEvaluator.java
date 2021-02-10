package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.api.data.entity.ArticleEntity;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.ArticleRepository;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.exception.UnauthorizedAccessException;
import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Permission;
import dev.codesupport.web.domain.ArticleRevision;
import dev.codesupport.web.domain.User;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Determines access for creating Article Revisions
 */
@Component
@EqualsAndHashCode(callSuper = true)
public class ArticleRevisionCreateEvaluator extends AbstractAccessEvaluator<ArticleRevision> {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public ArticleRevisionCreateEvaluator(UserRepository userRepository, ArticleRepository articleRepository) {
        super(Permission.CREATE);
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    /**
     * Checks if user has right to create an articleRevision
     * <p>Only authenticated users (with valid token) are allowed to create an articleRevision, as this is needed
     * to know who the user is.</p>
     *
     * @param auth            The Authentication associated with the access evaluation
     * @param articleRevision The object associated with the access evaluation
     * @return True if user is authenticated, False otherwise.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, ArticleRevision articleRevision) {
        ArticleEntity articleEntity = articleRepository.getById(articleRevision.getArticleId());
        boolean hasPermission = isValidAuth(auth);

        if (hasPermission) {
            // Set creator of showcase to the authenticated user.
            Optional<UserEntity> optional = userRepository.findByEmailIgnoreCase(auth.getName());
            if (optional.isPresent()) {
                UserEntity userEntity = optional.get();
                hasPermission = userEntity.getId().equals(articleEntity.getCreatedBy().getId())
                        || hasPrivilege(auth, "UPDATE_ARTICLE");

                if (hasPermission) {
                    User user = new User();
                    user.setId(userEntity.getId());
                    articleRevision.setCreatedBy(user);
                } else {
                    throw new UnauthorizedAccessException("You are not allowed to edit this article");
                }
            } else {
                throw new IllegalStateException("Could not access user's information");
            }
        }

        return hasPermission;
    }

}
