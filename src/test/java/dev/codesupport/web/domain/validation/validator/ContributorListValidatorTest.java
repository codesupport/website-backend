package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.testutils.validation.TestViolation;
import dev.codesupport.web.domain.Contributor;
import dev.codesupport.web.domain.ContributorList;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ContributorListValidatorTest {

    @Test
    public void shouldInvokeValidateContributorListWithNullPrefix() {
        ContributorListValidator validatorSpy = spy(ContributorListValidator.class);
        ContributorList contributorList = new ContributorList();
        TestViolation violation = new TestViolation();

        doNothing()
                .when(validatorSpy)
                .validateContributorList(any(), any(), any());

        validatorSpy.validate(contributorList, violation);

        verify(validatorSpy, times(1))
                .validateContributorList(contributorList, violation, null);
    }

    @Test
    public void shouldNotJoinNullValuesToCanonicalPath() {
        ContributorListValidator validator = new ContributorListValidator();
        ContributorList contributorList = new ContributorList();
        TestViolation violation = new TestViolation();

        validator.validateContributorList(contributorList, violation, null);

        Set<String> expected = Collections.singleton(
                ContributorList.Fields.contributors + " must contain at least 1 contributor"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindCorrectIssuesForValidator() {
        String prefix = "test";

        ContributorListValidator validator = new ContributorListValidator();
        ContributorList contributorList = new ContributorList();
        TestViolation violation = new TestViolation();

        validator.validateContributorList(contributorList, violation, prefix);

        Set<String> expected = Collections.singleton(
                String.join(".", prefix, ContributorList.Fields.contributors) +
                        " must contain at least 1 contributor"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindNoIssuesForValidator() {
        String prefix = "test";

        ContributorListValidator validatorSpy = spy(ContributorListValidator.class);
        ContributorList contributorList = new ContributorList();
        contributorList.setContributors(
                Arrays.asList(
                        new Contributor(),
                        new Contributor()
                )
        );
        TestViolation violation = new TestViolation();

        ContributorValidator mockContributorValidator = mock(ContributorValidator.class);

        doNothing()
                .when(mockContributorValidator)
                .validateContributor(any(), any(), any());

        doReturn(mockContributorValidator)
                .when(validatorSpy)
                .contributorValidator();

        validatorSpy.validateContributorList(contributorList, violation, prefix);

        Set<String> expected = Collections.emptySet();
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldInvokeContributorValidator() {
        String prefix = "test";

        ContributorListValidator validatorSpy = spy(ContributorListValidator.class);
        ContributorList contributorList = new ContributorList();
        contributorList.setContributors(
                Arrays.asList(
                        new Contributor(),
                        new Contributor()
                )
        );
        TestViolation violation = new TestViolation();

        ContributorValidator mockContributorValidator = mock(ContributorValidator.class);

        doNothing()
                .when(mockContributorValidator)
                .validate(any(), any());

        doReturn(mockContributorValidator)
                .when(validatorSpy)
                .contributorValidator();

        validatorSpy.validateContributorList(contributorList, violation, prefix);

        String contributorPrefix = String.join(
                ".",
                prefix,
                ContributorList.Fields.contributors + "[1]"
        );

        verify(mockContributorValidator, times(1))
                .validateContributor(new Contributor(), violation, contributorPrefix);
    }

    @Test
    public void shouldReturnNewInstanceOfContributorValidator() {
        ContributorListValidator validator = new ContributorListValidator();

        ContributorValidator instanceA = validator.contributorValidator();
        ContributorValidator instanceB = validator.contributorValidator();

        assertNotSame(instanceA, instanceB);
    }

}
