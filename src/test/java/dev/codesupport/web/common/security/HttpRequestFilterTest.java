package dev.codesupport.web.common.security;

import dev.codesupport.web.common.exception.InvalidTokenException;
import dev.codesupport.web.common.security.jwt.JsonWebToken;
import dev.codesupport.web.common.security.jwt.JsonWebTokenFactory;
import dev.codesupport.web.common.security.models.UserDetails;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

public class HttpRequestFilterTest {

    @Before
    public void setup() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void shouldInvokeCheckForJWToken() throws IOException, ServletException {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockFilterChain = mock(FilterChain.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        doNothing()
                .when(httpRequestFilterSpy)
                .checkForJWToken(any());

        doNothing()
                .when(mockFilterChain)
                .doFilter(any(), any());

        httpRequestFilterSpy.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(httpRequestFilterSpy, times(1))
                .checkForJWToken(mockRequest);
    }

    @Test
    public void shouldInvokeFilterChainDoFilter() throws IOException, ServletException {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockFilterChain = mock(FilterChain.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        doNothing()
                .when(httpRequestFilterSpy)
                .checkForJWToken(any());

        doNothing()
                .when(mockFilterChain)
                .doFilter(any(), any());

        httpRequestFilterSpy.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(mockFilterChain, times(1))
                .doFilter(mockRequest, mockResponse);
    }

    @Test
    public void shouldNotInvokeValidateTokenIfTokenNull() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        doReturn(null)
                .when(httpRequestFilterSpy)
                .getTokenFromRequest(mockRequest);

        doNothing()
                .when(httpRequestFilterSpy)
                .validateJWToken(any(), any());

        httpRequestFilterSpy.checkForJWToken(mockRequest);

        verify(httpRequestFilterSpy, times(0))
                .validateJWToken(any(), any());
    }

    @Test
    public void shouldNotInvokeValidateTokenIfTokenEmpty() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        doReturn("")
                .when(httpRequestFilterSpy)
                .getTokenFromRequest(mockRequest);

        doNothing()
                .when(httpRequestFilterSpy)
                .validateJWToken(any(), any());

        httpRequestFilterSpy.checkForJWToken(mockRequest);

        verify(httpRequestFilterSpy, times(0))
                .validateJWToken(any(), any());
    }

    @Test
    public void shouldInvokeValidateTokenIfTokenPresent() {
        String token = "token";
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        doReturn(token)
                .when(httpRequestFilterSpy)
                .getTokenFromRequest(mockRequest);

        doNothing()
                .when(httpRequestFilterSpy)
                .validateJWToken(any(), any());

        httpRequestFilterSpy.checkForJWToken(mockRequest);

        verify(httpRequestFilterSpy, times(1))
                .validateJWToken(token, mockRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfTokenStringIsNull() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        doNothing()
                .when(httpRequestFilterSpy)
                .configureSpringSecurityContext(any(), any());

        httpRequestFilterSpy.validateJWToken(null, mockRequest);
    }

    @Test
    public void shouldNotInvokeConfigureSpringSecurityContextIfInvalidTokenExceptionThrown() {
        String tokenString = "token";
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        doThrow(InvalidTokenException.class)
                .when(mockJsonWebTokenFactory)
                .createToken(tokenString);

        doNothing()
                .when(httpRequestFilterSpy)
                .configureSpringSecurityContext(any(), any());

        httpRequestFilterSpy.validateJWToken(tokenString, mockRequest);

        verify(httpRequestFilterSpy, times(0))
                .configureSpringSecurityContext(any(), any());
    }

    @Test
    public void shouldInvokeConfigureSpringSecurityContextIfValidToken() {
        String tokenString = "token";
        String email = "user@user.us";

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        JsonWebToken mockToken = mock(JsonWebToken.class);

        //ResultOfMethodCallIgnored - We're not getting the result, we're creating a mock.
        //noinspection ResultOfMethodCallIgnored
        doReturn(email)
                .when(mockToken)
                .getEmail();

        doReturn(mockToken)
                .when(mockJsonWebTokenFactory)
                .createToken(tokenString);

        doNothing()
                .when(httpRequestFilterSpy)
                .configureSpringSecurityContext(any(), any());

        httpRequestFilterSpy.validateJWToken(tokenString, mockRequest);

        verify(httpRequestFilterSpy, times(1))
                .configureSpringSecurityContext(email, mockRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfEmailIsNull() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        httpRequestFilterSpy.configureSpringSecurityContext(null, mockRequest);
    }

    @Test
    public void shouldNotReConfigureSpringSecurityContextIfPreviouslyConfigured() {
        String email = "user@user.us";

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        Authentication mockAuthentication = mock(Authentication.class);
        UserDetails mockUserDetails = mock(UserDetails.class);

        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        doReturn(mockUserDetails)
                .when(mockAuthorizationService)
                .getUserDetailsByEmail(email);

        httpRequestFilterSpy.configureSpringSecurityContext(email, mockRequest);

        Authentication actual = SecurityContextHolder.getContext().getAuthentication();

        assertEquals(mockAuthentication, actual);
    }

    @Test
    public void shouldConfigureSpringSecurityContextIfNotPreviouslyConfigured() {
        String email = "user@user.us";

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        UserDetails mockUserDetails = mock(UserDetails.class);

        doReturn(mockUserDetails)
                .when(mockAuthorizationService)
                .getUserDetailsByEmail(email);

        httpRequestFilterSpy.configureSpringSecurityContext(email, mockRequest);

        EmailPasswordAuthenticationToken expected = new EmailPasswordAuthenticationToken(
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
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        doReturn(null)
                .when(mockRequest)
                .getHeader(headerName);

        String actual = httpRequestFilterSpy.getTokenFromRequest(mockRequest);

        assertNull(actual);
    }

    @Test
    public void shouldReturnNullIfTokenEmpty() {
        String headerName = "Authorization";

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        doReturn("")
                .when(mockRequest)
                .getHeader(headerName);

        String actual = httpRequestFilterSpy.getTokenFromRequest(mockRequest);

        assertNull(actual);
    }

    @Test
    public void shouldReturnNullIfTokenInvalid() {
        String headerName = "Authorization";
        String headerValue = "Basic 1234";

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        doReturn(headerValue)
                .when(mockRequest)
                .getHeader(headerName);

        String actual = httpRequestFilterSpy.getTokenFromRequest(mockRequest);

        assertNull(actual);
    }

    @Test
    public void shouldReturnCorrectTokenIfTokenValid() {
        String expected = "1234";
        String headerName = "Authorization";
        String headerValue = "Bearer " + expected;

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        JsonWebTokenFactory mockJsonWebTokenFactory = mock(JsonWebTokenFactory.class);

        HttpRequestFilter httpRequestFilterSpy = spy(
                new HttpRequestFilter(mockJsonWebTokenFactory, mockAuthorizationService)
        );

        doReturn(headerValue)
                .when(mockRequest)
                .getHeader(headerName);

        String actual = httpRequestFilterSpy.getTokenFromRequest(mockRequest);

        assertEquals(expected, actual);
    }
}
