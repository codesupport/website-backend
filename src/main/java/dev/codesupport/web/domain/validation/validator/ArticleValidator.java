package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator;
import dev.codesupport.web.domain.validation.Violation;
import dev.codesupport.web.domain.validation.annotation.ArticleConstraint;
import org.apache.commons.lang3.StringUtils;

/**
 * Validation logic to be performed on properties annotated with {@link ArticleConstraint}
 */
public class ArticleValidator implements MultiViolationConstraintValidator<ArticleConstraint, Article> {

    private boolean idRequired;

    @Override
    public void initialize(ArticleConstraint constraintAnnotation) {
        idRequired = constraintAnnotation.requireId();
    }

    /**
     * Validates a {@link Article} object
     *
     * @param article   The {@link Article} object to validate
     * @param violation Reference to the violation helper object used for this validation
     */
    @Override
    public void validate(Article article, Violation violation) {
        if (idRequired) {
            if (article.getId() == null) {
                violation.nullValue(Article.Fields.id);
            } else if (article.getId() == 0){
                violation.invalid(Article.Fields.id);
            }
        }
        
        if (StringUtils.isBlank(article.getTitle())) {
            violation.missing(Article.Fields.title);
        }

        if (StringUtils.isBlank(article.getDescription())) {
            violation.missing(Article.Fields.description);
        }

        if (StringUtils.isBlank(article.getContent())) {
            violation.missing(Article.Fields.content);
        }
    }

}
