package dev.codesupport.web.domain.validation.validator;

import com.google.common.collect.Sets;
import dev.codesupport.testutils.validation.TestViolation;
import dev.codesupport.web.common.data.domain.AuditableDomain;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class AuditableDomainValidatorTest {

    @Test
    public void shouldFindCorrectIssuesForValidator() {
        AuditableDomainValidator validator = new AuditableDomainValidator();
        //unchecked - This is fine for the purposes of this test
        //noinspection unchecked
        AuditableDomain<Long, Long> mockAuditableDomain = mock(AuditableDomain.class);

        doReturn(null)
                .when(mockAuditableDomain)
                .getId();

        doReturn(null)
                .when(mockAuditableDomain)
                .getUpdatedOn();

        TestViolation violation = new TestViolation();

        validator.validate(mockAuditableDomain, violation);

        Set<String> expected = Sets.newHashSet(
                "id nullValue",
                "updatedOn nullValue"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindCorrectIssuesForValidatorWithInvalidId() {
        AuditableDomainValidator validator = new AuditableDomainValidator();
        //unchecked - This is fine for the purposes of this test
        //noinspection unchecked
        AuditableDomain<Long, Long> mockAuditableDomain = mock(AuditableDomain.class);

        doReturn(0L)
                .when(mockAuditableDomain)
                .getId();

        doReturn(1L)
                .when(mockAuditableDomain)
                .getUpdatedOn();

        TestViolation violation = new TestViolation();

        validator.validate(mockAuditableDomain, violation);

        Set<String> expected = Collections.singleton(
                "id invalid"
        );
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindNoIssuesForValidator() {
        AuditableDomainValidator validator = new AuditableDomainValidator();
        //unchecked - This is fine for the purposes of this test
        //noinspection unchecked
        AuditableDomain<Long, Long> mockIdentifiableDomain = mock(AuditableDomain.class);

        doReturn(1L)
                .when(mockIdentifiableDomain)
                .getId();

        doReturn(1L)
                .when(mockIdentifiableDomain)
                .getUpdatedOn();

        TestViolation violation = new TestViolation();

        validator.validate(mockIdentifiableDomain, violation);

        Set<String> expected = Collections.emptySet();
        Set<String> actual = violation.getViolations();

        assertEquals(expected, actual);
    }

}
