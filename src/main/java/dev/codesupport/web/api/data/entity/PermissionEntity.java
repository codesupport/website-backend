package dev.codesupport.web.api.data.entity;

import dev.codesupport.web.common.data.entity.IdentifiableEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class PermissionEntity implements IdentifiableEntity<Long> {

    @Id
    private Long id;
    @Column(unique = true, updatable = false, length = 20, nullable = false)
    private String code;
    @Column(updatable = false, length = 20, nullable = false)
    private String label;

}
