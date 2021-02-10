package dev.codesupport.web.common.data.entity;

import dev.codesupport.web.api.data.entity.UserEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class AuditEntity<T> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by", updatable = false)
    private UserEntity createdBy;
    @Column(updatable = false, nullable = false)
    private T createdOn;
    @ManyToOne(optional = false)
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;
    @Column(nullable = false)
    private T updatedOn;

}
