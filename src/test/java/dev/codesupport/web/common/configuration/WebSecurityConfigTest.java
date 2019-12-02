package dev.codesupport.web.common.configuration;

import dev.codesupport.web.common.security.JwtRequestFilter;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class WebSecurityConfigTest {

    @Test
    public void shouldReturnBCryptPasswordEncoderForPasswordEncoder() {
        SimpleAuthenticationEntryPoint mockSimpleAuthenticationEntryPoint = mock(SimpleAuthenticationEntryPoint.class);
        UserDetailsService mockUserDetailsService = mock(UserDetailsService.class);
        JwtRequestFilter mockJwtRequestFilter = mock(JwtRequestFilter.class);
        AuthenticationProvider mockAuthenticationProvider = mock(AuthenticationProvider.class);

        WebSecurityConfig webSecurityConfigSpy = spy(
                new WebSecurityConfig(
                        mockSimpleAuthenticationEntryPoint,
                        mockUserDetailsService,
                        mockJwtRequestFilter,
                        mockAuthenticationProvider
                )
        );

        assertTrue(webSecurityConfigSpy.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void shouldReturnNewInstanceOfEncoder() {
        SimpleAuthenticationEntryPoint mockSimpleAuthenticationEntryPoint = mock(SimpleAuthenticationEntryPoint.class);
        UserDetailsService mockUserDetailsService = mock(UserDetailsService.class);
        JwtRequestFilter mockJwtRequestFilter = mock(JwtRequestFilter.class);
        AuthenticationProvider mockAuthenticationProvider = mock(AuthenticationProvider.class);

        WebSecurityConfig webSecurityConfigSpy = spy(
                new WebSecurityConfig(
                        mockSimpleAuthenticationEntryPoint,
                        mockUserDetailsService,
                        mockJwtRequestFilter,
                        mockAuthenticationProvider
                )
        );

        PasswordEncoder firstInstance = webSecurityConfigSpy.passwordEncoder();
        PasswordEncoder secondInstance = webSecurityConfigSpy.passwordEncoder();

        assertNotSame(firstInstance, secondInstance);
    }
}
