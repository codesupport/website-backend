package dev.codesupport.web.api.data.entity;

import dev.codesupport.web.common.data.entity.IdentifiableEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class RoleEntity implements IdentifiableEntity<Long> {

    @Id
    private Long id;
    @Column(unique = true, updatable = false, length = 20, nullable = false)
    private String code;
    @Column(length = 20, nullable = false)
    private String label;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<PermissionEntity> permission = new HashSet<>();

}
