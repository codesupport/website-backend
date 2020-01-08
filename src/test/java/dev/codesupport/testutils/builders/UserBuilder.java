package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.domain.User;

//unused - Used for unit tests, not everything will be used
@SuppressWarnings("unused")
public class UserBuilder {

    private Long id;
    private String alias;
    private String hashPassword;
    private String email;
    private String avatarLink;
    private Long joinDate;

    private UserBuilder() {

    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public User buildDomain() {
        User user = new User();
        user.setId(id);
        user.setAlias(alias);
        user.setHashPassword(hashPassword);
        user.setEmail(email);
        user.setAvatarLink(avatarLink);
        user.setJoinDate(joinDate);
        return user;
    }

    public UserEntity buildEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setAlias(alias);
        userEntity.setHashPassword(hashPassword);
        userEntity.setEmail(email);
        userEntity.setAvatarLink(avatarLink);
        userEntity.setJoinDate(joinDate);
        return userEntity;
    }

    public UserBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder alias(String alias) {
        this.alias = alias;
        return this;
    }

    public UserBuilder hashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder avatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
        return this;
    }

    public UserBuilder joinDate(Long joinDate) {
        this.joinDate = joinDate;
        return this;
    }
}
