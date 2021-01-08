package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.PublishedArticle;
import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator;
import dev.codesupport.web.domain.validation.Violation;
import dev.codesupport.web.domain.validation.annotation.PublishedArticleConstraint;

/**
 * Validation logic to be performed on properties annotated with {@link PublishedArticleConstraint}
 */
public class PublishedArticleValidator implements MultiViolationConstraintValidator<PublishedArticleConstraint, PublishedArticle> {

    /**
     * Validates a {@link PublishedArticle} object
     *
     * @param publishedArticle The {@link PublishedArticle} object to validate
     * @param violation        Reference to the violation helper object used for this validation
     */
    @Override
    public void validate(PublishedArticle publishedArticle, Violation violation) {
        if (publishedArticle.getId() == null) {
            violation.nullValue(PublishedArticle.Fields.id);
        } else if (publishedArticle.getId() < 1) {
            violation.invalid(PublishedArticle.Fields.id);
        }

        if (publishedArticle.getArticle() == null) {
            violation.nullValue(PublishedArticle.Fields.article);
        } else {
            if (publishedArticle.getArticle().getUpdatedOn() == null) {
                violation.nullValue(PublishedArticle.Fields.article + "." + Article.Fields.updatedOn);
            }
        }
    }

}
