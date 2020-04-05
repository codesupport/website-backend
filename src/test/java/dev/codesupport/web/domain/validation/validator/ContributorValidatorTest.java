package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.testutils.validation.TestViolation;
import dev.codesupport.web.domain.Contributor;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ContributorValidatorTest {

    @Test
    public void shouldInvokeValidateContributorWithNullPrefix() {
        ContributorValidator validatorSpy = spy(ContributorValidator.class);
        Contributor contributor = new Contributor();
        TestViolation violation = new TestViolation();

        validatorSpy.validate(contributor, violation);

        verify(validatorSpy, times(1))
                .validateContributor(contributor, violation, null);
    }

    @Test
    public void shouldNotJoinNullValuesToCanonicalPath() {
        ContributorValidator validator = new ContributorValidator();
        Contributor contributor = new Contributor();
        TestViolation violation = new TestViolation();

        validator.validateContributor(contributor, violation, null);

        Set<String> expected = Collections.singleton(
                Contributor.Fields.alias + " must include alias or user id"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindCorrectIssuesForValidator() {
        String prefix = "test";

        ContributorValidator validator = new ContributorValidator();
        Contributor contributor = new Contributor();
        TestViolation violation = new TestViolation();

        validator.validateContributor(contributor, violation, prefix);

        Set<String> expected = Collections.singleton(
                String.join(
                        ".",
                        prefix,
                        Contributor.Fields.alias + " must include alias or user id"
                )
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindNoIssuesForValidator() {
        String prefix = "test";

        ContributorValidator validator = new ContributorValidator();
        Contributor contributor = new Contributor();
        contributor.setAlias("smith");
        TestViolation violation = new TestViolation();

        validator.validateContributor(contributor, violation, prefix);

        Set<String> expected = Collections.emptySet();
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

}
