package dev.codesupport.web.common.security;

import dev.codesupport.web.common.exception.InvalidTokenException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
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
    public void shouldInvokeCheckForJWToken() throws IOException, ServletException {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockFilterChain = mock(FilterChain.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        doNothing()
                .when(jwtRequestFilterSpy)
                .checkForJWToken(any());

        doNothing()
                .when(mockFilterChain)
                .doFilter(any(), any());

        jwtRequestFilterSpy.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(jwtRequestFilterSpy, times(1))
                .checkForJWToken(mockRequest);
    }

    @Test
    public void shouldInvokeFilterChainDoFilter() throws IOException, ServletException {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockFilterChain = mock(FilterChain.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        doNothing()
                .when(jwtRequestFilterSpy)
                .checkForJWToken(any());

        doNothing()
                .when(mockFilterChain)
                .doFilter(any(), any());

        jwtRequestFilterSpy.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(mockFilterChain, times(1))
                .doFilter(mockRequest, mockResponse);
    }

    @Test
    public void shouldNotInvokeValidateTokenIfTokenNull() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        doReturn(null)
                .when(jwtRequestFilterSpy)
                .getTokenFromRequest(mockRequest);

        doNothing()
                .when(jwtRequestFilterSpy)
                .validateJWToken(any(), any());

        jwtRequestFilterSpy.checkForJWToken(mockRequest);

        verify(jwtRequestFilterSpy, times(0))
                .validateJWToken(any(), any());
    }

    @Test
    public void shouldNotInvokeValidateTokenIfTokenEmpty() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        doReturn("")
                .when(jwtRequestFilterSpy)
                .getTokenFromRequest(mockRequest);

        doNothing()
                .when(jwtRequestFilterSpy)
                .validateJWToken(any(), any());

        jwtRequestFilterSpy.checkForJWToken(mockRequest);

        verify(jwtRequestFilterSpy, times(0))
                .validateJWToken(any(), any());
    }

    @Test
    public void shouldInvokeValidateTokenIfTokenPresent() {
        String token = "token";
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        doReturn(token)
                .when(jwtRequestFilterSpy)
                .getTokenFromRequest(mockRequest);

        doNothing()
                .when(jwtRequestFilterSpy)
                .validateJWToken(any(), any());

        jwtRequestFilterSpy.checkForJWToken(mockRequest);

        verify(jwtRequestFilterSpy, times(1))
                .validateJWToken(token, mockRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfTokenStringIsNull() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        doNothing()
                .when(jwtRequestFilterSpy)
                .configureSpringSecurityContext(any(), any());

        jwtRequestFilterSpy.validateJWToken(null, mockRequest);
    }

    @Test
    public void shouldNotInvokeConfigureSpringSecurityContextIfInvalidTokenExceptionThrown() {
        String tokenString = "token";
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        doThrow(InvalidTokenException.class)
                .when(mockJsonWebTokenFactory)
                .createToken(tokenString);

        doNothing()
                .when(jwtRequestFilterSpy)
                .configureSpringSecurityContext(any(), any());

        jwtRequestFilterSpy.validateJWToken(tokenString, mockRequest);

        verify(jwtRequestFilterSpy, times(0))
                .configureSpringSecurityContext(any(), any());
    }

    @Test
    public void shouldInvokeConfigureSpringSecurityContextIfValidToken() {
        String tokenString = "token";
        String username = "user";
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        JsonWebToken mockToken = mock(JsonWebToken.class);

        //ResultOfMethodCallIgnored - We're not getting the result, we're creating a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn(username)
                .when(mockToken)
                .getUsername();

        doReturn(mockToken)
                .when(mockJsonWebTokenFactory)
                .createToken(tokenString);

        doNothing()
                .when(jwtRequestFilterSpy)
                .configureSpringSecurityContext(any(), any());

        jwtRequestFilterSpy.validateJWToken(tokenString, mockRequest);

        verify(jwtRequestFilterSpy, times(1))
                .configureSpringSecurityContext(username, mockRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfUsernameIsNull() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        jwtRequestFilterSpy.configureSpringSecurityContext(null, mockRequest);
    }

    @Test
    public void shouldNotReConfigureSpringSecurityContextIfPreviouslyConfigured() {
        String username = "user";
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        Authentication mockAuthentication = mock(Authentication.class);
        UserDetails mockUserDetails = mock(UserDetails.class);

        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        doReturn(mockUserDetails)
                .when(mockUserDetailsService)
                .loadUserByUsername(username);

        jwtRequestFilterSpy.configureSpringSecurityContext(username, mockRequest);

        Authentication actual = SecurityContextHolder.getContext().getAuthentication();

        assertEquals(mockAuthentication, actual);
    }

    @Test
    public void shouldConfigureSpringSecurityContextIfNotPreviouslyConfigured() {
        String username = "user";
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        UserDetails mockUserDetails = mock(UserDetails.class);

        doReturn(mockUserDetails)
                .when(mockUserDetailsService)
                .loadUserByUsername(username);

        jwtRequestFilterSpy.configureSpringSecurityContext(username, mockRequest);

        UsernamePasswordAuthenticationToken expected = new UsernamePasswordAuthenticationToken(
                mockUserDetails,
                null,
                mockUserDetails.getAuthorities()
        );
        expected.setDetails(new WebAuthenticationDetailsSource().buildDetails(mockRequest));

        Authentication actual = SecurityContextHolder.getContext().getAuthentication();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnNullIfTokenNotSet() {
        String headerName = "Authorization";

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        doReturn(null)
                .when(mockRequest)
                .getHeader(headerName);

        String actual = jwtRequestFilterSpy.getTokenFromRequest(mockRequest);

        assertNull(actual);
    }

    @Test
    public void shouldReturnNullIfTokenEmpty() {
        String headerName = "Authorization";

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthenticationUserDetailsService mockUserDetailsService = mock(AuthenticationUserDetailsService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        doReturn("")
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
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

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
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        JwtRequestFilter jwtRequestFilterSpy = spy(new JwtRequestFilter(mockUserDetailsService, mockJsonWebTokenFactory));

        doReturn(headerValue)
                .when(mockRequest)
                .getHeader(headerName);

        String actual = jwtRequestFilterSpy.getTokenFromRequest(mockRequest);

        assertEquals(expected, actual);
    }
}
