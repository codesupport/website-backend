package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.domain.User;

//unused - Used for unit tests, not everything will be used
@SuppressWarnings("unused")
public class UserBuilder {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String avatarLink;
    private Long addedBy;
    private Long joinDate;

    private UserBuilder() {

    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public User buildDomain() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setAvatarLink(avatarLink);
        user.setAddedBy(addedBy);
        user.setJoinDate(joinDate);
        return user;
    }

    public UserEntity buildEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setEmail(email);
        userEntity.setAvatarLink(avatarLink);
        userEntity.setAddedBy(addedBy);
        userEntity.setJoinDate(joinDate);
        return userEntity;
    }

    public UserBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
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

    public UserBuilder addedBy(Long addedBy) {
        this.addedBy = addedBy;
        return this;
    }

    public UserBuilder joinDate(Long joinDate) {
        this.joinDate = joinDate;
        return this;
    }
}
