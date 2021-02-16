package dev.codesupport.web.domain.validation.validator;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.ArticleRevision;
import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator;
import dev.codesupport.web.domain.validation.Violation;
import dev.codesupport.web.domain.validation.annotation.ArticleConstraint;
import org.apache.commons.lang3.StringUtils;

/**
 * Validation logic to be performed on properties annotated with {@link ArticleConstraint}
 */
public class ArticleValidator implements MultiViolationConstraintValidator<ArticleConstraint, Article> {

    private boolean isCreate;

    @Override
    public void initialize(ArticleConstraint constraintAnnotation) {
        isCreate = constraintAnnotation.isCreate();
    }

    /**
     * Validates a {@link Article} object
     *
     * @param article   The {@link Article} object to validate
     * @param violation Reference to the violation helper object used for this validation
     */
    @Override
    public void validate(Article article, Violation violation) {
        if (isCreate) {
            createValidations(article, violation);
        } else {
            updateValidations(article, violation);
        }
    }

    @VisibleForTesting
    void createValidations(Article article, Violation violation) {
        article.setId(null);
        article.setTitleId(null);
        article.setRevision(null);

        if (StringUtils.isBlank(article.getTitle())) {
            violation.missing(Article.Fields.title);
        }
    }

    @VisibleForTesting
    void updateValidations(Article article, Violation violation) {
        if (article.getId() == null) {
            violation.nullValue(Article.Fields.id);
        } else if (article.getId() == 0) {
            violation.invalid(Article.Fields.id);
        }

        if (article.getRevision() != null) {
            if (article.getRevision().getId() == null) {
                violation.nullValue(Article.Fields.revision + "." + ArticleRevision.Fields.id);
            } else if (article.getRevision().getId() < 0) {
                violation.invalid(Article.Fields.revision + "." + ArticleRevision.Fields.id);
            }
        }

        if (article.getUpdatedOn() == null) {
            violation.nullValue(Article.Fields.updatedOn);
        }
    }

}
