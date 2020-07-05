package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.PermissionEntity;
import dev.codesupport.web.domain.Permission;

//unused - Used for unit tests, not everything will be used
@SuppressWarnings("unused")
public class PermissionBuilder {

    private Long id;
    private String code;
    private String label;

    private PermissionBuilder() {

    }

    public static PermissionBuilder builder() {
        return new PermissionBuilder();
    }

    public Permission buildDomain() {
        Permission domain = new Permission();
        domain.setId(id);
        domain.setCode(code);
        domain.setLabel(label);
        return domain;
    }

    public String buildPrivilege() {
        return code;
    }

    public PermissionEntity buildEntity() {
        PermissionEntity entity = new PermissionEntity();
        entity.setId(id);
        entity.setCode(code);
        entity.setLabel(label);
        return entity;
    }

    public PermissionBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public PermissionBuilder code(String code) {
        this.code = code;
        return this;
    }

    public PermissionBuilder label(String label) {
        this.label = label;
        return this;
    }
}
