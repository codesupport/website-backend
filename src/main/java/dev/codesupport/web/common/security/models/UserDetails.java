package dev.codesupport.web.common.security.models;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Defines various user details that will be used for authentication/access purposes
 */
@Data
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private final Long id;
    private final String alias;
    private final String password;
    private final String email;
    private final Set<String> privileges;
    private final boolean disabled;
    private final boolean verified;

    private Collection<GrantedAuthority> authorities;

    /**
     * Concatenates all permissions into a single "authories" collection.
     * <p>Here to be called for use with spring to set application security context.</p>
     *
     * @return Collection of authorities
     */
    public Collection<GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = Lists.newArrayList(privileges.iterator())
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return alias;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !disabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return StringUtils.isNoneBlank(password);
    }

    @Override
    public boolean isEnabled() {
        return verified;
    }
}
