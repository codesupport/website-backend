package dev.codesupport.web.common.security.models;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Defines various user details that will be used for authentication/access purposes
 */
@Data
public class UserDetails {

    private final Long id;
    private final String alias;
    private final String password;
    private final String email;
    private final Set<String> privileges;
    private final boolean disabled;

    private Collection<SimpleGrantedAuthority> authorities;

    /**
     * Concatenates all permissions into a single "authories" collection.
     * <p>Here to be called for use with spring to set application security context.</p>
     *
     * @return Collection of authorities
     */
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = Lists.newArrayList(privileges.iterator())
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return authorities;
    }
}
