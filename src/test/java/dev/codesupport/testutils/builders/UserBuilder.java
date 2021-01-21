package dev.codesupport.testutils.builders;

import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.common.security.models.UserDetails;
import dev.codesupport.web.domain.NewUser;
import dev.codesupport.web.domain.User;
import dev.codesupport.web.domain.UserProfile;
import dev.codesupport.web.domain.UserRegistration;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.stream.Collectors;

//unused - Used for unit tests, not everything will be used
@SuppressWarnings("unused")
public class UserBuilder {

    private Long id;
    private String alias;
    private String hashPassword;
    private String verifyToken;
    private String password;
    private String discordId;
    private String discordUsername;
    private String githubUsername;
    private String jobTitle;
    private String jobCompany;
    private String email;
    private String avatarLink;
    private boolean disabled;
    private RoleBuilder role;
    private Set<PermissionBuilder> permission;
    private String biography;
    private CountryBuilder country;
    private Set<UserAwardBuilder> userAward;
    private Long joinDate;

    private UserBuilder() {

    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public User buildDomain() {
        User domain = new User();
        domain.setId(id);
        domain.setAlias(alias);
        domain.setDiscordId(discordId);
        domain.setDiscordUsername(discordUsername);
        domain.setAvatarLink(avatarLink);
        domain.setDisabled(disabled);
        if (role != null)
            domain.setRole(role.buildDomain());
        domain.setJoinDate(joinDate);
        return domain;
    }

    public NewUser buildNewUserDomain() {
        NewUser domain = new NewUser();
        domain.setId(id);
        domain.setAlias(alias);
        domain.setPassword(password);
        domain.setHashPassword(hashPassword);
        domain.setEmail(email);
        domain.setDiscordId(discordId);
        domain.setDiscordUsername(discordUsername);
        domain.setAvatarLink(avatarLink);
        domain.setDisabled(disabled);
        if (role != null)
            domain.setRole(role.buildDomain());
        domain.setJoinDate(joinDate);
        return domain;
    }

    public UserProfile buildUserProfileDomain() {
        UserProfile domain = new UserProfile();
        domain.setId(id);
        domain.setAlias(alias);
        domain.setDiscordId(discordId);
        domain.setDiscordUsername(discordUsername);
        domain.setGithubUsername(githubUsername);
        domain.setJobTitle(jobTitle);
        domain.setJobCompany(jobCompany);
        domain.setEmail(email);
        domain.setAvatarLink(avatarLink);
        domain.setDisabled(disabled);
        if (role != null)
            domain.setRole(role.buildDomain());
        domain.setBiography(biography);
        if (country != null)
            domain.setCountry(country.buildDomain());
        if (userAward != null)
            domain.setUserAward(userAward.stream().map(UserAwardBuilder::buildDomain).collect(Collectors.toSet()));
        domain.setJoinDate(joinDate);
        return domain;
    }

    public UserDetails buildUserDetailsDomain() {
        return new UserDetails(
                id,
                alias,
                hashPassword,
                email,
                permission.stream().map(PermissionBuilder::buildPrivilege).collect(Collectors.toSet()),
                disabled,
                StringUtils.isBlank(verifyToken)
        );
    }

    public UserRegistration buildUserRegistrationDomain() {
        UserRegistration domain = new UserRegistration();
        domain.setAlias(alias);
        domain.setPassword(password);
        domain.setEmail(email);
        return domain;
    }

    public UserEntity buildEntity() {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setAlias(alias);
        entity.setHashPassword(hashPassword);
        entity.setVerifyToken(verifyToken);
        entity.setDiscordId(discordId);
        entity.setDiscordUsername(discordUsername);
        entity.setGithubUsername(githubUsername);
        entity.setJobTitle(jobTitle);
        entity.setJobCompany(jobCompany);
        entity.setEmail(email);
        entity.setAvatarLink(avatarLink);
        entity.setDisabled(disabled);
        if (role != null)
            entity.setRole(role.buildEntity());
        if (permission != null)
            entity.setPermission(permission.stream().map(PermissionBuilder::buildEntity).collect(Collectors.toSet()));
        entity.setBiography(biography);
        if (country != null)
            entity.setCountry(country.buildEntity());
        if (userAward != null)
            entity.setUserAward(userAward.stream().map(UserAwardBuilder::buildEntity).collect(Collectors.toSet()));
        entity.setJoinDate(joinDate);
        return entity;
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

    public UserBuilder verifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder discordId(String discordId) {
        this.discordId = discordId;
        return this;
    }

    public UserBuilder discordUsername(String discordUsername) {
        this.discordUsername = discordUsername;
        return this;
    }

    public UserBuilder githubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
        return this;
    }

    public UserBuilder jobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public UserBuilder jobCompany(String jobCompany) {
        this.jobCompany = jobCompany;
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

    public UserBuilder disabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public UserBuilder role(RoleBuilder role) {
        this.role = role;
        return this;
    }

    public UserBuilder permission(Set<PermissionBuilder> permission) {
        this.permission = permission;
        return this;
    }

    public UserBuilder biography(String biography) {
        this.biography = biography;
        return this;
    }

    public UserBuilder country(CountryBuilder country) {
        this.country = country;
        return this;
    }

    public UserBuilder userAward(Set<UserAwardBuilder> userAward) {
        this.userAward = userAward;
        return this;
    }

    public UserBuilder joinDate(Long joinDate) {
        this.joinDate = joinDate;
        return this;
    }
}
