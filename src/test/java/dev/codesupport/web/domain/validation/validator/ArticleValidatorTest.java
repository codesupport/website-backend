package dev.codesupport.web.domain.validation.validator;

import com.google.common.collect.Sets;
import dev.codesupport.testutils.builders.ArticleBuilder;
import dev.codesupport.testutils.builders.ArticleRevisionBuilder;
import dev.codesupport.testutils.validation.TestViolation;
import dev.codesupport.web.domain.Article;
import dev.codesupport.web.domain.ArticleRevision;
import dev.codesupport.web.domain.validation.annotation.ArticleConstraint;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ArticleValidatorTest {

    @Test
    public void shouldSetIsCreateToTrueFromAnnotation() {
        ArticleConstraint mockConstraint = mock(ArticleConstraint.class);

        doReturn(true)
                .when(mockConstraint)
                .isCreate();

        ArticleValidator validator = new ArticleValidator();
        validator.initialize(mockConstraint);

        Object actual = ReflectionTestUtils.getField(validator, "isCreate");

        assertEquals(true, actual);
    }

    @Test
    public void shouldSetIsCreateToFalseFromAnnotation() {
        ArticleConstraint mockConstraint = mock(ArticleConstraint.class);

        doReturn(false)
                .when(mockConstraint)
                .isCreate();

        ArticleValidator validator = new ArticleValidator();
        validator.initialize(mockConstraint);

        Object actual = ReflectionTestUtils.getField(validator, "isCreate");

        assertEquals(false, actual);
    }

    @Test
    public void shouldInvokeCreateValidationsOnCreate() {
        ArticleValidator validatorSpy = spy(ArticleValidator.class);

        ReflectionTestUtils.setField(validatorSpy, "isCreate", true);

        Article mockArticle = mock(Article.class);

        TestViolation mockViolation = mock(TestViolation.class);

        doNothing()
                .when(validatorSpy)
                .createValidations(any(), any());

        doNothing()
                .when(validatorSpy)
                .updateValidations(any(), any());

        validatorSpy.validate(mockArticle, mockViolation);

        verify(validatorSpy, times(1))
                .createValidations(mockArticle, mockViolation);

        verify(validatorSpy, times(0))
                .updateValidations(any(), any());
    }

    @Test
    public void shouldInvokeUpdateValidationsOnCreate() {
        ArticleValidator validatorSpy = spy(ArticleValidator.class);

        ReflectionTestUtils.setField(validatorSpy, "isCreate", false);

        Article mockArticle = mock(Article.class);

        TestViolation mockViolation = mock(TestViolation.class);

        doNothing()
                .when(validatorSpy)
                .createValidations(any(), any());

        doNothing()
                .when(validatorSpy)
                .updateValidations(any(), any());

        validatorSpy.validate(mockArticle, mockViolation);

        verify(validatorSpy, times(1))
                .updateValidations(any(), any());

        verify(validatorSpy, times(0))
                .createValidations(mockArticle, mockViolation);
    }

    @Test
    public void shouldFindCorrectIssuesForValidatorOnCreate() {
        ArticleValidator validator = new ArticleValidator();

        Article article = new Article();

        TestViolation violation = new TestViolation();

        validator.createValidations(article, violation);

        Set<String> expected = Sets.newHashSet(
                Article.Fields.title + " missing"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldSetUndefinableAttributesToNullOnCreate() {
        ArticleValidator validator = new ArticleValidator();

        ArticleBuilder builder = ArticleBuilder.builder()
                .id(15L)
                .title("Some title")
                .titleId("some-title")
                .revision(
                        ArticleRevisionBuilder.builder()
                        .id(10L)
                );

        Article actual = builder.buildDomain();
        Article expected = builder.buildDomain();
        expected.setId(null);
        expected.setTitleId(null);
        expected.setRevision(null);

        TestViolation violation = new TestViolation();

        validator.createValidations(actual, violation);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindCorrectIssuesForValidatorOnUpdate() {
        ArticleValidator validator = new ArticleValidator();

        Article article = ArticleBuilder.builder()
                .revision(ArticleRevisionBuilder.builder())
                .buildDomain();

        TestViolation violation = new TestViolation();

        validator.updateValidations(article, violation);

        Set<String> expected = Sets.newHashSet(
                Article.Fields.updatedOn + " nullValue",
                Article.Fields.revision + "." + ArticleRevision.Fields.id + " nullValue",
                Article.Fields.id + " nullValue"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindCorrectIssuesForValidatorOnUpdateIdsInvalid() {
        ArticleValidator validator = new ArticleValidator();

        Article article = ArticleBuilder.builder()
                .id(0L)
                .title("Some title")
                .revision(
                        ArticleRevisionBuilder.builder()
                        .id(-1L)
                )
                .buildDomain();

        TestViolation violation = new TestViolation();

        validator.updateValidations(article, violation);

        Set<String> expected = Sets.newHashSet(
                Article.Fields.id + " invalid",
                Article.Fields.revision + "." + ArticleRevision.Fields.id + " invalid",
                Article.Fields.updatedOn + " nullValue"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

}
