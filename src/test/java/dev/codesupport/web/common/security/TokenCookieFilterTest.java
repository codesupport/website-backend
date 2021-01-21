package dev.codesupport.web.common.security;

import dev.codesupport.testutils.builders.CookieBuilder;
import dev.codesupport.testutils.builders.PermissionBuilder;
import dev.codesupport.testutils.builders.UserBuilder;
import dev.codesupport.web.api.data.entity.PermissionEntity;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.common.configuration.HttpSessionProperties;
import dev.codesupport.web.common.security.models.UserDetails;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TokenCookieFilterTest {

    private SecurityContext context;

    @Before
    public void setUp() {
        context = SecurityContextHolder.getContext();
    }

    @After
    public void tearDown() {
        SecurityContextHolder.setContext(context);
    }

    @Test
    public void shouldNotSetContextIfAuthUriRequest() throws ServletException, IOException {
        String uri = "/abc";
        String cookieName = "name";
        String cookieValue = "value";

        TokenCookieFilter filterSpy = spy(new TokenCookieFilter(null, null));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockFilterChain = mock(FilterChain.class);

        doReturn(uri)
                .when(mockRequest)
                .getRequestURI();

        Cookie cookie = new Cookie(cookieName, cookieValue);

        doReturn(Optional.of(cookie))
                .when(filterSpy)
                .pullTokenCookieFromRequest(mockRequest);

        doNothing()
                .when(filterSpy)
                .setContextForToken(any(), any(), any());

        doReturn(false)
                .when(filterSpy)
                .isNotAuthenticateEndpoint(uri);

        filterSpy.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(filterSpy, times(0))
                .setContextForToken(any(), any(), any());
    }

    @Test
    public void shouldNotSetContextIfTokenCookieNotInRequest() throws ServletException, IOException {
        String uri = "/abc";

        TokenCookieFilter filterSpy = spy(new TokenCookieFilter(null, null));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockFilterChain = mock(FilterChain.class);

        doReturn(uri)
                .when(mockRequest)
                .getRequestURI();

        doReturn(Optional.empty())
                .when(filterSpy)
                .pullTokenCookieFromRequest(mockRequest);

        doNothing()
                .when(filterSpy)
                .setContextForToken(any(), any(), any());

        doReturn(false)
                .when(filterSpy)
                .isNotAuthenticateEndpoint(uri);

        filterSpy.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(filterSpy, times(0))
                .setContextForToken(any(), any(), any());
    }

    @Test
    public void shouldSetContextIfNotAuthUriRequest() throws ServletException, IOException {
        String uri = "/abc";
        String cookieName = "name";
        String cookieValue = "value";

        TokenCookieFilter filterSpy = spy(new TokenCookieFilter(null, null));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockFilterChain = mock(FilterChain.class);

        doReturn(uri)
                .when(mockRequest)
                .getRequestURI();

        Cookie cookie = new Cookie(cookieName, cookieValue);

        doReturn(Optional.of(cookie))
                .when(filterSpy)
                .pullTokenCookieFromRequest(mockRequest);

        doNothing()
                .when(filterSpy)
                .setContextForToken(any(), any(), any());

        doReturn(true)
                .when(filterSpy)
                .isNotAuthenticateEndpoint(uri);

        filterSpy.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(filterSpy, times(1))
                .setContextForToken(any(), any(), any());

        verify(filterSpy, times(1))
                .setContextForToken(mockRequest, mockResponse, cookieValue);
    }

    @Test
    public void shouldExecuteFilterChain() throws ServletException, IOException {
        String uri = "/abc";
        String cookieName = "name";
        String cookieValue = "value";

        TokenCookieFilter filterSpy = spy(new TokenCookieFilter(null, null));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockFilterChain = mock(FilterChain.class);

        doReturn(uri)
                .when(mockRequest)
                .getRequestURI();

        Cookie cookie = new Cookie(cookieName, cookieValue);

        doReturn(Optional.of(cookie))
                .when(filterSpy)
                .pullTokenCookieFromRequest(mockRequest);

        doNothing()
                .when(filterSpy)
                .setContextForToken(any(), any(), any());

        doReturn(true)
                .when(filterSpy)
                .isNotAuthenticateEndpoint(uri);

        filterSpy.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(mockFilterChain, times(1))
                .doFilter(any(), any());

        verify(mockFilterChain, times(1))
                .doFilter(mockRequest, mockResponse);
    }

    @Test
    public void shouldReturnFalseForIsNotAuthenticateEndpoint() {
        TokenCookieFilter filterSpy = spy(new TokenCookieFilter(null, null));

        assertTrue(filterSpy.isNotAuthenticateEndpoint("/authenticate/abc"));
    }

    @Test
    public void shouldReturnTrueForIsNotAuthenticateEndpoint() {
        TokenCookieFilter filterSpy = spy(new TokenCookieFilter(null, null));

        assertFalse(filterSpy.isNotAuthenticateEndpoint("/authenticate/////"));
    }

    @Test
    public void shouldReturnEmptyOptionalIfNoCookies() {
        String cookieName = "name";

        HttpSessionProperties mockHttpSessionProperties = mock(HttpSessionProperties.class);

        HttpSessionProperties.CookieConfiguration mockCookieConfig = mock(HttpSessionProperties.CookieConfiguration.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(cookieName)
                .when(mockCookieConfig)
                .getName();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(mockCookieConfig)
                .when(mockHttpSessionProperties)
                .getCookie();

        TokenCookieFilter filterSpy = spy(new TokenCookieFilter(null, null));

        ReflectionTestUtils.setField(filterSpy, "httpSessionProperties", mockHttpSessionProperties);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        doReturn(null)
                .when(mockRequest)
                .getCookies();

        Optional<Cookie> expected = Optional.empty();
        Optional<Cookie> actual = filterSpy.pullTokenCookieFromRequest(mockRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldEmptyOptionalIfCookieNotFoundInRequest() {
        String cookieName = "name";

        HttpSessionProperties mockHttpSessionProperties = mock(HttpSessionProperties.class);

        HttpSessionProperties.CookieConfiguration mockCookieConfig = mock(HttpSessionProperties.CookieConfiguration.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(cookieName)
                .when(mockCookieConfig)
                .getName();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(mockCookieConfig)
                .when(mockHttpSessionProperties)
                .getCookie();

        TokenCookieFilter filterSpy = spy(new TokenCookieFilter(null, null));

        ReflectionTestUtils.setField(filterSpy, "httpSessionProperties", mockHttpSessionProperties);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        doReturn(new Cookie[]{CookieBuilder.builder().name("one").value("two").build()})
                .when(mockRequest)
                .getCookies();

        Optional<Cookie> expected = Optional.empty();
        Optional<Cookie> actual = filterSpy.pullTokenCookieFromRequest(mockRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldPullCookieFromRequest() {
        String cookieName = "name";
        String cookieValue = "value";

        HttpSessionProperties mockHttpSessionProperties = mock(HttpSessionProperties.class);

        HttpSessionProperties.CookieConfiguration mockCookieConfig = mock(HttpSessionProperties.CookieConfiguration.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(cookieName)
                .when(mockCookieConfig)
                .getName();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(mockCookieConfig)
                .when(mockHttpSessionProperties)
                .getCookie();

        TokenCookieFilter filterSpy = spy(new TokenCookieFilter(null, null));

        ReflectionTestUtils.setField(filterSpy, "httpSessionProperties", mockHttpSessionProperties);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        Cookie[] requestCookies = new Cookie[]{
                CookieBuilder.builder().name("one").value("two").build(),
                CookieBuilder.builder().name(cookieName).value(cookieValue).build()
        };

        doReturn(requestCookies)
                .when(mockRequest)
                .getCookies();

        Optional<Cookie> expected = Optional.of(requestCookies[1]);
        Optional<Cookie> actual = filterSpy.pullTokenCookieFromRequest(mockRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotInvokeConfigureSecurityContextOrSetHeaderForExpiredToken() {
        String token = "token";
        String newToken = "newToken";

        AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);

        TokenCookieFilter filterSpy = spy(new TokenCookieFilter(null, null));

        ReflectionTestUtils.setField(filterSpy, "authenticationService", mockAuthenticationService);

        UserEntity mockUserEntity = mock(UserEntity.class);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        UserDetails mockUserDetails = mock(UserDetails.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(Long.MIN_VALUE)
                .when(mockUserEntity)
                .getAccessTokenExpireOn();

        doReturn(null)
                .when(mockAuthenticationService)
                .getUserByToken(any());

        doReturn(mockUserEntity)
                .when(mockAuthenticationService)
                .getUserByToken(token);

        doReturn(null)
                .when(mockAuthenticationService)
                .createTokenCookieForUser(any());

        doReturn(newToken)
                .when(mockAuthenticationService)
                .createTokenCookieForUser(mockUserEntity);

        doReturn(null)
                .when(filterSpy)
                .createUserDetails(any());

        doReturn(mockUserDetails)
                .when(filterSpy)
                .createUserDetails(mockUserEntity);

        doNothing()
                .when(mockResponse)
                .setHeader(any(), any());

        doNothing()
                .when(filterSpy)
                .configureSpringSecurityContext(any(), any());

        filterSpy.setContextForToken(mockRequest, mockResponse, token);

        verify(filterSpy, times(0))
                .configureSpringSecurityContext(any(), any());

        verify(mockResponse, times(0))
                .setHeader(any(), any());
    }

    @Test
    public void shouldInvokeConfigureSecurityContextAndSetHeaderForExpiredToken() {
        String token = "token";
        String newToken = "newToken";

        AuthenticationService mockAuthenticationService = mock(AuthenticationService.class);

        TokenCookieFilter filterSpy = spy(new TokenCookieFilter(null, null));

        ReflectionTestUtils.setField(filterSpy, "authenticationService", mockAuthenticationService);

        UserEntity mockUserEntity = mock(UserEntity.class);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        UserDetails mockUserDetails = mock(UserDetails.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(Long.MAX_VALUE)
                .when(mockUserEntity)
                .getAccessTokenExpireOn();

        doReturn(null)
                .when(mockAuthenticationService)
                .getUserByToken(any());

        doReturn(mockUserEntity)
                .when(mockAuthenticationService)
                .getUserByToken(token);

        doReturn(null)
                .when(mockAuthenticationService)
                .createTokenCookieForUser(any());

        doReturn(newToken)
                .when(mockAuthenticationService)
                .createTokenCookieForUser(mockUserEntity);

        doReturn(null)
                .when(filterSpy)
                .createUserDetails(any());

        doReturn(mockUserDetails)
                .when(filterSpy)
                .createUserDetails(mockUserEntity);

        doNothing()
                .when(mockResponse)
                .setHeader(any(), any());

        doNothing()
                .when(filterSpy)
                .configureSpringSecurityContext(any(), any());

        filterSpy.setContextForToken(mockRequest, mockResponse, token);

        verify(filterSpy, times(1))
                .configureSpringSecurityContext(mockRequest, mockUserDetails);

        verify(mockResponse, times(1))
                .setHeader("Set-Cookie", newToken);
    }

    @Test
    public void shouldSetSecurityContext() {
        SecurityContext mockContext = mock(SecurityContext.class);

        SecurityContextHolder.setContext(mockContext);

        TokenCookieFilter filterSpy = spy(new TokenCookieFilter(null, null));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        UserDetails mockUserDetails = mock(UserDetails.class);

        List<GrantedAuthority> authorities = Collections.singletonList(mock(GrantedAuthority.class));

        doReturn(authorities)
                .when(mockUserDetails)
                .getAuthorities();

        filterSpy.configureSpringSecurityContext(mockRequest, mockUserDetails);

        EmailPasswordAuthenticationToken expectedAuthToken = new EmailPasswordAuthenticationToken(
                mockUserDetails, null, authorities);
        expectedAuthToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(mockRequest));

        verify(mockContext, times(1))
                .setAuthentication(expectedAuthToken);
    }

    @Test
    public void shouldReturnUserDetailsObject() {
        TokenCookieFilter filter = new TokenCookieFilter(null, null);

        UserEntity userEntity = UserBuilder.builder()
                .id(5L)
                .alias("alias")
                .hashPassword("Password")
                .email("email")
                .permission(
                        Collections.singleton(
                                PermissionBuilder.builder()
                                        .id(10L)
                                        .code("code")
                                        .label("label")
                        )
                )
                .disabled(true)
                .verifyToken("tokentokentoken")
                .buildEntity();

        UserDetails expected = new UserDetails(
                userEntity.getId(),
                userEntity.getAlias(),
                userEntity.getHashPassword(),
                userEntity.getEmail(),
                userEntity.getPermission().stream().map(PermissionEntity::getCode).collect(Collectors.toSet()),
                userEntity.isDisabled(),
                StringUtils.isBlank(userEntity.getVerifyToken())
        );
        UserDetails actual = filter.createUserDetails(userEntity);

        assertEquals(expected, actual);
    }

}
