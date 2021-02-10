package dev.codesupport.web.common.data.entity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AuditableListener {

    @PrePersist
    public void onCreate(AuditableEntity<?,?> auditableEntity) {
        AuditEntity auditEntity = auditableEntity.getAuditEntity();
        if (auditEntity == null) {
            auditEntity = new AuditEntity<>();
            auditableEntity.setAuditEntity(auditEntity);
        }

        auditEntity.setCreatedOn(System.currentTimeMillis());
        onUpdate(auditableEntity);
    }

    @PreUpdate
    public void onUpdate(AuditableEntity<?,?> auditableEntity) {
        AuditEntity auditEntity = auditableEntity.getAuditEntity();
        if (auditEntity == null) {
            auditEntity = new AuditEntity<>();
            auditableEntity.setAuditEntity(auditEntity);
        }

        auditEntity.setUpdatedOn(System.currentTimeMillis());
    }

}
