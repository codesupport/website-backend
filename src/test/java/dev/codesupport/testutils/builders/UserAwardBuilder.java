package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.UserAwardEntity;
import dev.codesupport.web.domain.UserAward;

//unused - Used for unit tests, not everything will be used
@SuppressWarnings("unused")
public class UserAwardBuilder {

    private Long id;
    private String code;
    private String label;
    private String description;

    private UserAwardBuilder() {

    }

    public static UserAwardBuilder builder() {
        return new UserAwardBuilder();
    }

    public UserAward buildDomain() {
        UserAward domain = new UserAward();
        domain.setId(id);
        domain.setCode(code);
        domain.setLabel(label);
        domain.setDescription(description);
        return domain;
    }

    public UserAwardEntity buildEntity() {
        UserAwardEntity entity = new UserAwardEntity();
        entity.setId(id);
        entity.setCode(code);
        entity.setLabel(label);
        entity.setDescription(description);
        return entity;
    }

    public UserAwardBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UserAwardBuilder code(String code) {
        this.code = code;
        return this;
    }

    public UserAwardBuilder label(String label) {
        this.label = label;
        return this;
    }

    public UserAwardBuilder description(String description) {
        this.description = description;
        return this;
    }
}
