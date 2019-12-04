package dev.codesupport.web.common.service.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

/**
 * Gathers information for UserDetails of authenticating user.
 */
@Service
public class AuthenticationUserDetailsService implements UserDetailsService {

    /**
     * Gets details of user to authenticate
     * This is stubbed out for now.
     *
     * @param username The username associated with the requested user details
     * @return The UserDetails object populated with desired info.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        //TODO: Remove stub, add true implementation, getting details from persistent storage.
        return new UserDetails() {
            private static final long serialVersionUID = 2139825498003613413L;

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Arrays.asList(
                        new SimpleGrantedAuthority("ROLE_MEMBER"),
                        new SimpleGrantedAuthority("ROLE_ADMIN")
                );
            }

            @Override
            public String getPassword() {
                return "admin";
            }

            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

}
