package dev.codesupport.web.domain.validation.validator;

import com.google.common.collect.Sets;
import dev.codesupport.testutils.builders.ArticleRevisionBuilder;
import dev.codesupport.testutils.validation.TestViolation;
import dev.codesupport.web.domain.ArticleRevision;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ArticleRevisionValidatorTest {

    @Test
    public void shouldFindCorrectIssuesForValidator() {
        ArticleRevisionValidator validator = new ArticleRevisionValidator();
        ArticleRevision articleRevision = new ArticleRevision();

        TestViolation violation = new TestViolation();

        validator.validate(articleRevision, violation);

        Set<String> expected = Sets.newHashSet(
                ArticleRevision.Fields.content + " missing",
                ArticleRevision.Fields.description + " missing",
                ArticleRevision.Fields.articleId + " nullValue"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindCorrectIssuesForValidatorIfInvalidId() {
        ArticleRevisionValidator validator = new ArticleRevisionValidator();

        ArticleRevision articleRevision = ArticleRevisionBuilder.builder()
                .articleId(-1L)
                .description("Some description")
                .content("Some content")
                .buildDomain();

        TestViolation violation = new TestViolation();

        validator.validate(articleRevision, violation);

        Set<String> expected = Sets.newHashSet(
                ArticleRevision.Fields.articleId + " invalid"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindNoIssuesForValidator() {
        ArticleRevisionValidator validator = new ArticleRevisionValidator();
        ArticleRevision articleRevision = ArticleRevisionBuilder.builder()
                .articleId(10L)
                .description("description")
                .content("content")
                .buildDomain();

        TestViolation violation = new TestViolation();

        validator.validate(articleRevision, violation);

        Set<String> expected = Collections.emptySet();
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

}
