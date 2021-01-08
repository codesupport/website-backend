package dev.codesupport.web.domain.validation.validator;

import com.google.common.collect.Sets;
import dev.codesupport.testutils.builders.ArticleBuilder;
import dev.codesupport.testutils.validation.TestViolation;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.validation.annotation.ArticleConstraint;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ArticleValidatorTest {

    @Test
    public void shouldSetIdRequiredToFalseFromAnnotation() {
        ArticleConstraint mockConstraint = mock(ArticleConstraint.class);

        doReturn(false)
                .when(mockConstraint)
                .requireId();

        ArticleValidator validator = new ArticleValidator();
        validator.initialize(mockConstraint);

        Object actual = ReflectionTestUtils.getField(validator, "idRequired");

        assertEquals(false, actual);
    }

    @Test
    public void shouldSetIdRequiredToTrueFromAnnotation() {
        ArticleConstraint mockConstraint = mock(ArticleConstraint.class);

        doReturn(true)
                .when(mockConstraint)
                .requireId();

        ArticleValidator validator = new ArticleValidator();
        validator.initialize(mockConstraint);

        Object actual = ReflectionTestUtils.getField(validator, "idRequired");

        assertEquals(true, actual);
    }

    @Test
    public void shouldFindCorrectIssuesForValidator() {
        ArticleValidator validator = new ArticleValidator();
        Article article = new Article();

        TestViolation violation = new TestViolation();

        validator.validate(article, violation);

        Set<String> expected = Sets.newHashSet(
                Article.Fields.content + " missing",
                Article.Fields.description + " missing",
                Article.Fields.title + " missing"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindCorrectIssuesForValidatorIfIdRequired() {
        ArticleValidator validator = new ArticleValidator();

        ReflectionTestUtils.setField(validator, "idRequired", true);

        Article article = new Article();

        TestViolation violation = new TestViolation();

        validator.validate(article, violation);

        Set<String> expected = Sets.newHashSet(
                Article.Fields.id + " nullValue",
                Article.Fields.content + " missing",
                Article.Fields.description + " missing",
                Article.Fields.title + " missing"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindCorrectIssuesForValidatorIfIdInvalidAndRequired() {
        ArticleValidator validator = new ArticleValidator();

        ReflectionTestUtils.setField(validator, "idRequired", true);

        Article article = new Article();
        article.setId(0L);

        TestViolation violation = new TestViolation();

        validator.validate(article, violation);

        Set<String> expected = Sets.newHashSet(
                Article.Fields.id + " invalid",
                Article.Fields.content + " missing",
                Article.Fields.description + " missing",
                Article.Fields.title + " missing"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindNoIssuesForValidator() {
        ArticleValidator validator = new ArticleValidator();
        Article article = ArticleBuilder.builder()
                .title("title")
                .description("description")
                .content("content")
                .buildDomain();

        TestViolation violation = new TestViolation();

        validator.validate(article, violation);

        Set<String> expected = Collections.emptySet();
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

}
