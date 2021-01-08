package dev.codesupport.web.domain.validation.validator;

import dev.codesupport.web.common.data.domain.AuditableDomain;
import dev.codesupport.web.domain.validation.MultiViolationConstraintValidator;
import dev.codesupport.web.domain.validation.Violation;
import dev.codesupport.web.domain.validation.annotation.AuditableDomainConstraint;

/**
 * Validation logic to be performed on properties annotated with {@link AuditableDomainConstraint}
 */
public class AuditableDomainValidator implements MultiViolationConstraintValidator<AuditableDomainConstraint, AuditableDomain<Long, Long>> {

    /**
     * Validates a {@link AuditableDomain} object
     *
     * @param auditableDomain The {@link AuditableDomain} object to validate
     * @param violation       Reference to the violation helper object used for this validation
     */
    @Override
    public void validate(AuditableDomain<Long, Long> auditableDomain, Violation violation) {
        if (auditableDomain.getId() == null) {
            violation.nullValue("id");
        } else if (auditableDomain.getId() < 1) {
            violation.invalid("id");
        }

        if (auditableDomain.getUpdatedOn() == null) {
            violation.nullValue("updatedOn");
        }
    }

}
