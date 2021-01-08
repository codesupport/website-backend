package dev.codesupport.web.domain.validation.validator;

import com.google.common.collect.Sets;
import dev.codesupport.testutils.builders.ArticleBuilder;
import dev.codesupport.testutils.builders.PublishedArticleBuilder;
import dev.codesupport.testutils.validation.TestViolation;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.PublishedArticle;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PublishedArticleValidatorTest {

    @Test
    public void shouldFindCorrectIssuesForValidator() {
        PublishedArticleValidator validator = new PublishedArticleValidator();
        PublishedArticle publishedArticle = new PublishedArticle();

        TestViolation violation = new TestViolation();

        validator.validate(publishedArticle, violation);

        Set<String> expected = Sets.newHashSet(
                PublishedArticle.Fields.id + " nullValue",
                PublishedArticle.Fields.article + " nullValue"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindCorrectIssuesForValidatorIfIdInvalid() {
        PublishedArticleValidator validator = new PublishedArticleValidator();
        PublishedArticle publishedArticle = PublishedArticleBuilder.builder()
                .id(0L)
                .article(
                        ArticleBuilder.builder()
                )
                .buildDomain();

        TestViolation violation = new TestViolation();

        validator.validate(publishedArticle, violation);

        Set<String> expected = Sets.newHashSet(
                PublishedArticle.Fields.id + " invalid",
                PublishedArticle.Fields.article + "." + Article.Fields.updatedOn + " nullValue"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindNoIssuesForValidator() {
        PublishedArticleValidator validator = new PublishedArticleValidator();
        PublishedArticle publishedArticle = PublishedArticleBuilder.builder()
                .id(10L)
                .article(
                        ArticleBuilder.builder()
                        .updatedOn(5L)
                )
                .buildDomain();

        TestViolation violation = new TestViolation();

        validator.validate(publishedArticle, violation);

        Set<String> expected = Collections.emptySet();
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

}
