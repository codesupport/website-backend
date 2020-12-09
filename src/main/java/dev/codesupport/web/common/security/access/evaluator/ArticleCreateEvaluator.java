package dev.codesupport.web.common.security.access.evaluator;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.api.data.repository.UserRepository;
import dev.codesupport.web.common.security.access.AbstractAccessEvaluator;
import dev.codesupport.web.common.security.access.Permission;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.User;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Determines access for creating Articles
 */
@Component
@EqualsAndHashCode(callSuper = true)
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
            Optional<UserEntity> optional = userRepository.findByEmailIgnoreCase(auth.getName());
            if (optional.isPresent()) {
                UserEntity userEntity = optional.get();
                User user = new User();
                user.setId(userEntity.getId());
                article.setCreatedBy(user);
                article.setUpdatedBy(user);
            } else {
                throw new IllegalStateException("Could not access user's information");
            }
        }

        return hasPermission;
    }

}
