package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.testutils.validation.TestViolation;
import dev.codesupport.web.common.data.domain.IdentifiableDomain;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class IdentifiableDomainValidatorTest {

    @Test
    public void shouldFindCorrectIssuesForValidator() {
        IdentifiableDomainValidator validator = new IdentifiableDomainValidator();
        //unchecked - This is fine for the purposes of this test
        //noinspection unchecked
        IdentifiableDomain<Long> mockIdentifiableDomain = mock(IdentifiableDomain.class);
        doReturn(null)
                .when(mockIdentifiableDomain)
                .getId();
        TestViolation violation = new TestViolation();

        validator.validate(mockIdentifiableDomain, violation);

        Set<String> expected = Collections.singleton(
                "id nullValue"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindNoIssuesForValidator() {
        IdentifiableDomainValidator validator = new IdentifiableDomainValidator();
        //unchecked - This is fine for the purposes of this test
        //noinspection unchecked
        IdentifiableDomain<Long> mockIdentifiableDomain = mock(IdentifiableDomain.class);
        doReturn(1L)
                .when(mockIdentifiableDomain)
                .getId();
        TestViolation violation = new TestViolation();

        validator.validate(mockIdentifiableDomain, violation);

        Set<String> expected = Collections.emptySet();
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

}
