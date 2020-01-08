package dev.codesupport.web.api.data.entity;

import dev.codesupport.web.common.service.data.entity.IdentifiableEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Data
@Entity
public class RoleEntity implements IdentifiableEntity<Long> {

    @Id
    private Long id;
    private String code;
    private String label;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<PermissionEntity> permission;

}
