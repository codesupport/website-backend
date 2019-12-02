package dev.codesupport.web.common.security;

import dev.codesupport.web.common.service.service.AuthenticationUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JwtRequestFilterTest {

    @Before
    public void setup() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void shouldInvokeConfigureSpringSecurityContext() throws IOException, ServletException {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockFilterChain = mock(FilterChain.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService));

        doNothing()
                .when(jwtRequestFilterSpy)
                .configureSpringSecurityContext(mockRequest);

        doNothing()
                .when(mockFilterChain)
                .doFilter(mockRequest, mockResponse);

        jwtRequestFilterSpy.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(jwtRequestFilterSpy, times(1))
                .configureSpringSecurityContext(mockRequest);
    }

    @Test
    public void shouldInvokeFilterChainDoFilter() throws IOException, ServletException {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockFilterChain = mock(FilterChain.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService));

        doNothing()
                .when(jwtRequestFilterSpy)
                .configureSpringSecurityContext(mockRequest);

        doNothing()
                .when(mockFilterChain)
                .doFilter(mockRequest, mockResponse);

        jwtRequestFilterSpy.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(mockFilterChain, times(1))
                .doFilter(mockRequest, mockResponse);
    }

    @Test
    public void shouldConfigureSpringSecurityContextWithNoAutheticationIfTokenNull() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService));

        doReturn(null)
                .when(jwtRequestFilterSpy)
                .getTokenFromRequest(mockRequest);

        jwtRequestFilterSpy.configureSpringSecurityContext(mockRequest);

        Authentication actual = SecurityContextHolder.getContext().getAuthentication();

        assertNull(actual);
    }

    @Test
    public void shouldConfigureSpringSecurityContextWithCorrectAutheticationIfTokenValid() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        UserDetails mockUserDetails = mock(UserDetails.class);

        doReturn(mockUserDetails)
                .when(mockUserDetailsService)
                .loadUserByUsername("admin");

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService));

        doReturn("1234")
                .when(jwtRequestFilterSpy)
                .getTokenFromRequest(mockRequest);

        jwtRequestFilterSpy.configureSpringSecurityContext(mockRequest);

        UsernamePasswordAuthenticationToken expected = new UsernamePasswordAuthenticationToken(
                mockUserDetails, null, mockUserDetails.getAuthorities());
        expected
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(mockRequest));

        Authentication actual = SecurityContextHolder.getContext().getAuthentication();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnNullIfTokenNotSet() {
        String headerName = "Authorization";

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService));

        doReturn(null)
                .when(mockRequest)
                .getHeader(headerName);

        String actual = jwtRequestFilterSpy.getTokenFromRequest(mockRequest);

        assertNull(actual);
    }

    @Test
    public void shouldReturnNullIfTokenInvalid() {
        String headerName = "Authorization";
        String headerValue = "Basic 1234";

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService));

        doReturn(headerValue)
                .when(mockRequest)
                .getHeader(headerName);

        String actual = jwtRequestFilterSpy.getTokenFromRequest(mockRequest);

        assertNull(actual);
    }

    @Test
    public void shouldReturnCorrectTokenIfTokenValid() {
        String expected = "1234";
        String headerName = "Authorization";
        String headerValue = "Bearer " + expected;

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService));

        doReturn(headerValue)
                .when(mockRequest)
                .getHeader(headerName);

        String actual = jwtRequestFilterSpy.getTokenFromRequest(mockRequest);

        assertEquals(expected, actual);
    }
}
