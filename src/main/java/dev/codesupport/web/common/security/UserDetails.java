package dev.codesupport.web.common.security;

import com.google.api.client.util.Lists;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDetails {

    private final String alias;
    private final String password;
    private final String email;
    private final Set<String> privileges;
    private final boolean disabled;

    private Collection<SimpleGrantedAuthority> authorities;

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
