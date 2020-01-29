package dev.codesupport.web.common.security;

import dev.codesupport.web.common.security.models.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Token used with the spring authentication context
 * <p>Exists simply so I can override getName() for a better response.</p>
 */
public class EmailPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public EmailPasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public EmailPasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    /**
     * Gets the name associated to the authentication token
     *
     * @return Email if principal is {@link UserDetails}, else returns default implementation.
     */
    @Override
    public String getName() {
        String name;

        if (this.getPrincipal() instanceof UserDetails) {
            name = ((UserDetails) this.getPrincipal()).getEmail();
        } else {
            name = super.getName();
        }

        return name;
    }
}
