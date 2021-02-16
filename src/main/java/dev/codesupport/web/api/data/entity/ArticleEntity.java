package dev.codesupport.web.api.data.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dev.codesupport.web.common.data.entity.AuditEntity;
import dev.codesupport.web.common.data.entity.AuditableEntity;
import dev.codesupport.web.common.data.entity.AuditableListener;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@EntityListeners(AuditableListener.class)
public class ArticleEntity implements AuditableEntity<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long revisionId;
    @Column(updatable = false, length = 100, nullable = false)
    private String title;
    @Column(unique = true, updatable = false, length = 100, nullable = false)
    private String titleId;
    @JsonUnwrapped
    @Embedded
    private AuditEntity<Long> auditEntity = new AuditEntity<>();

}
