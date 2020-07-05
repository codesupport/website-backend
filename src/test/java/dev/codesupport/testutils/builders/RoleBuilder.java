package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.RoleEntity;
import dev.codesupport.web.domain.Role;

import java.util.Set;
import java.util.stream.Collectors;

//unused - Used for unit tests, not everything will be used
@SuppressWarnings("unused")
public class RoleBuilder {

    private Long id;
    private String code;
    private String label;
    private Set<PermissionBuilder> permission;

    private RoleBuilder() {

    }

    public static RoleBuilder builder() {
        return new RoleBuilder();
    }

    public Role buildDomain() {
        Role domain = new Role();
        domain.setId(id);
        domain.setCode(code);
        domain.setLabel(label);
        if (permission != null) {
            domain.setPermission(permission.stream().map(PermissionBuilder::buildDomain).collect(Collectors.toSet()));
        }
        return domain;
    }

    public RoleEntity buildEntity() {
        RoleEntity entity = new RoleEntity();
        entity.setId(id);
        entity.setCode(code);
        entity.setLabel(label);
        if (permission != null) {
            entity.setPermission(permission.stream().map(PermissionBuilder::buildEntity).collect(Collectors.toSet()));
        }
        return entity;
    }

    public RoleBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public RoleBuilder code(String code) {
        this.code = code;
        return this;
    }

    public RoleBuilder label(String label) {
        this.label = label;
        return this;
    }

    public RoleBuilder permission(Set<PermissionBuilder> permission) {
        this.permission = permission;
        return this;
    }
}
