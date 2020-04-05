package dev.codesupport.web.domain.validation.validator;

import com.google.common.collect.Sets;
import dev.codesupport.testutils.validation.TestViolation;
import dev.codesupport.web.domain.ContributorList;
import dev.codesupport.web.domain.Showcase;
import org.junit.Test;

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

public class ShowcaseValidatorTest {

    @Test
    public void shouldFindCorrectIssuesForValidator() {
        ShowcaseValidator validator = new ShowcaseValidator();
        Showcase showcase = new Showcase();
        TestViolation violation = new TestViolation();

        validator.validate(showcase, violation);

        Set<String> expected = Sets.newHashSet(
                Showcase.Fields.description + " missing",
                Showcase.Fields.title + " missing",
                Showcase.Fields.contributorList + " missing",
                Showcase.Fields.link + " missing"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindNoIssuesForValidator() {
        ShowcaseValidator validatorSpy = spy(ShowcaseValidator.class);
        Showcase showcase = new Showcase();
        showcase.setDescription("description");
        showcase.setTitle("title");
        showcase.setContributorList(new ContributorList());
        showcase.setLink("link");
        TestViolation violation = new TestViolation();

        ContributorListValidator mockContributorListValidator = mock(ContributorListValidator.class);

        doNothing()
                .when(mockContributorListValidator)
                .validate(any(), any());

        doReturn(mockContributorListValidator)
                .when(validatorSpy)
                .contributorListValidator();

        validatorSpy.validate(showcase, violation);

        Set<String> expected = Collections.emptySet();
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldInvokeContributorListValidator() {
        ShowcaseValidator validatorSpy = spy(ShowcaseValidator.class);
        Showcase showcase = new Showcase();
        showcase.setDescription("description");
        showcase.setTitle("title");
        showcase.setContributorList(new ContributorList());
        showcase.setLink("link");
        TestViolation violation = new TestViolation();

        ContributorListValidator mockContributorListValidator = mock(ContributorListValidator.class);

        doNothing()
                .when(mockContributorListValidator)
                .validate(any(), any());

        doReturn(mockContributorListValidator)
                .when(validatorSpy)
                .contributorListValidator();

        validatorSpy.validate(showcase, violation);

        String contributorListPrefix = Showcase.Fields.contributorList;

        verify(mockContributorListValidator, times(1))
                .validateContributorList(showcase.getContributorList(), violation, contributorListPrefix);
    }

    @Test
    public void shouldReturnNewInstance() {
        ShowcaseValidator validator = new ShowcaseValidator();

        ContributorListValidator instanceA = validator.contributorListValidator();
        ContributorListValidator instanceB = validator.contributorListValidator();

        assertNotSame(instanceA, instanceB);
    }

}
