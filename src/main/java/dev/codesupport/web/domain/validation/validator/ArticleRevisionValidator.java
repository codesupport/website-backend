package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.domain.ArticleRevision;
import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator;
import dev.codesupport.web.domain.validation.Violation;
import dev.codesupport.web.domain.validation.annotation.ArticleRevisionConstraint;
import org.apache.commons.lang3.StringUtils;

/**
 * Validation logic to be performed on properties annotated with {@link ArticleRevisionConstraint}
 */
public class ArticleRevisionValidator implements MultiViolationConstraintValidator<ArticleRevisionConstraint, ArticleRevision> {

    /**
     * Validates a {@link ArticleRevision} object
     *
     * @param articleRevision The {@link ArticleRevision} object to validate
     * @param violation       Reference to the violation helper object used for this validation
     */
    @Override
    public void validate(ArticleRevision articleRevision, Violation violation) {
        articleRevision.setId(null);

        if (articleRevision.getArticleId() == null) {
            violation.nullValue(ArticleRevision.Fields.articleId);
        } else if (articleRevision.getArticleId() < 1) {
            violation.invalid(ArticleRevision.Fields.articleId);
        }

        if (StringUtils.isBlank(articleRevision.getDescription())) {
            violation.missing(ArticleRevision.Fields.description);
        }

        if (StringUtils.isBlank(articleRevision.getContent())) {
            violation.missing(ArticleRevision.Fields.content);
        }
    }

}
