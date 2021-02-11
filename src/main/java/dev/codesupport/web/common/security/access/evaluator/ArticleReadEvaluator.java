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
 * Determines access for viewing an Article
 */
@Component
@EqualsAndHashCode(callSuper = true)
public class ArticleReadEvaluator extends AbstractAccessEvaluator<Long> {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public ArticleReadEvaluator(
            UserRepository userRepository,
            ArticleRepository articleRepository
    ) {
        super(Permission.READ);
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    /**
     * Checks if user has right to see the article
     * <p>Stubbed permission logic, only admins and the author can see a non-published.</p>
     *
     * @param auth          The Authentication associated with the access evaluation
     * @param articleId The target article's id
     * @return True if user is allowed to see the article, False otherwise.
     */
    @Override
    protected boolean hasPermissionCheck(Authentication auth, Long articleId) {
        ArticleEntity articleEntity = articleRepository.getById(articleId);

        boolean hasPermission = articleEntity.getRevisionId() != null;

        if (!hasPermission && isValidAuth(auth)) {
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
        return Accessor.ARTICLE;
    }
}
