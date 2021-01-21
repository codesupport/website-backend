package dev.codesupport.web.common.security;

import dev.codesupport.web.common.security.models.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Objects;

/**
 * Token used with the spring authentication context
 * <p>Exists simply so I can override getName() for a better response.</p>
 */
public class EmailPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public EmailPasswordAuthenticationToken(UserDetails principal, Object credentials) {
        this(principal, credentials, null);
    }

    public EmailPasswordAuthenticationToken(UserDetails principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    /**
     * Gets {@link UserDetails} from the local thread security context
     *
     * @return The {@link UserDetails} object from the principal
     */
    @Override
    public Object getDetails() {
        Object name;

        if (this.getPrincipal() instanceof UserDetails) {
            name = this.getPrincipal();
        } else {
            name = super.getDetails();
        }

        return name;
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
            throw new IllegalStateException("This can't happen");
        }

        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof EmailPasswordAuthenticationToken)) {
            return false;
        } else {
            EmailPasswordAuthenticationToken other = (EmailPasswordAuthenticationToken) o;
            return Objects.equals(getPrincipal(), other.getPrincipal())
                    && Objects.equals(getCredentials(), other.getCredentials())
                    && Objects.equals(getAuthorities(), other.getAuthorities());
        }
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + (getPrincipal() == null ? 43 : getPrincipal().hashCode());
        result = result * 59 + (getCredentials() == null ? 43 : getCredentials().hashCode());
        result = result * 59 + (getAuthorities() == null ? 43 : getAuthorities().hashCode());
        return result;
    }
}
