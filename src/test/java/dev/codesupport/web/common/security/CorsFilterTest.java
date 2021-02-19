package dev.codesupport.web.common.security;

import dev.codesupport.web.common.configuration.HttpSessionProperties;
import org.assertj.core.util.Sets;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CorsFilterTest {

    @Test
    public void shouldAddHeadersToResponse() throws ServletException, IOException {
        String origin = "*";
        String method = "*";
        Set<String> headers = Sets.newLinkedHashSet(
                "headerA",
                "headerB"
        );

        HttpSessionProperties mockHttpSessionProperties = mock(HttpSessionProperties.class);
        HttpSessionProperties.CorsConfiguration mockCorsConfig = mock(HttpSessionProperties.CorsConfiguration.class);

        CorsFilter filterSpy = spy(new CorsFilter(mockHttpSessionProperties));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockFilterChain = mock(FilterChain.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(Collections.singleton(origin))
                .when(mockCorsConfig)
                .getOrigins();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(Collections.singleton(method))
                .when(mockCorsConfig)
                .getMethods();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(headers)
                .when(mockCorsConfig)
                .getHeaders();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(true)
                .when(mockCorsConfig)
                .isCredentialsAllowed();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(mockCorsConfig)
                .when(mockHttpSessionProperties)
                .getCors();

        doReturn(Optional.of(origin))
                .when(filterSpy)
                .accessControlAllowOrigins(mockRequest, Collections.singleton(origin));

        doReturn(method)
                .when(filterSpy)
                .accessControlAllowFor(Collections.singleton(method));

        doNothing()
                .when(mockResponse)
                .setHeader(any(), any());

        doNothing()
                .when(mockFilterChain)
                .doFilter(any(), any());

        filterSpy.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(mockResponse, times(1))
                .setHeader("Access-Control-Allow-Origin", "*");

        verify(mockResponse, times(1))
                .setHeader("Access-Control-Allow-Methods", "*");

        verify(mockResponse, times(1))
                .setHeader("Access-Control-Allow-Headers", "headerA, headerB");

        verify(mockResponse, times(1))
                .setHeader("Access-Control-Allow-Credentials", "true");

        verify(mockResponse, times(4))
                .setHeader(any(), any());
    }

    @Test
    public void shouldNotAddHeadersToResponse() throws ServletException, IOException {
        String origin = "*";
        String method = "*";

        HttpSessionProperties mockHttpSessionProperties = mock(HttpSessionProperties.class);
        HttpSessionProperties.CorsConfiguration mockCorsConfig = mock(HttpSessionProperties.CorsConfiguration.class);

        CorsFilter filterSpy = spy(new CorsFilter(mockHttpSessionProperties));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockFilterChain = mock(FilterChain.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(Collections.singleton(origin))
                .when(mockCorsConfig)
                .getOrigins();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(Collections.singleton(method))
                .when(mockCorsConfig)
                .getMethods();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(mockCorsConfig)
                .when(mockHttpSessionProperties)
                .getCors();

        doReturn(Optional.empty())
                .when(filterSpy)
                .accessControlAllowOrigins(mockRequest, Collections.singleton(origin));

        doReturn(method)
                .when(filterSpy)
                .accessControlAllowFor(Collections.singleton(method));

        doNothing()
                .when(mockResponse)
                .setHeader(any(), any());

        doNothing()
                .when(mockFilterChain)
                .doFilter(any(), any());

        filterSpy.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(mockResponse, times(0))
                .setHeader(any(), any());
    }

    @Test
    public void shouldInvokeFilterChain() throws ServletException, IOException {
        String origin = "*";
        String method = "*";

        HttpSessionProperties mockHttpSessionProperties = mock(HttpSessionProperties.class);
        HttpSessionProperties.CorsConfiguration mockCorsConfig = mock(HttpSessionProperties.CorsConfiguration.class);

        CorsFilter filterSpy = spy(new CorsFilter(mockHttpSessionProperties));

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        FilterChain mockFilterChain = mock(FilterChain.class);

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(Collections.singleton(origin))
                .when(mockCorsConfig)
                .getOrigins();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(Collections.singleton(method))
                .when(mockCorsConfig)
                .getMethods();

        //ResultOfMethodCallIgnored - Not invoking a method, creating a mock
        //noinspection ResultOfMethodCallIgnored
        doReturn(mockCorsConfig)
                .when(mockHttpSessionProperties)
                .getCors();

        doReturn(Optional.of(origin))
                .when(filterSpy)
                .accessControlAllowOrigins(mockRequest, Collections.singleton(origin));

        doReturn(method)
                .when(filterSpy)
                .accessControlAllowFor(Collections.singleton(method));

        doNothing()
                .when(mockResponse)
                .setHeader(any(), any());

        doNothing()
                .when(mockFilterChain)
                .doFilter(any(), any());

        filterSpy.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        verify(mockFilterChain, times(1))
                .doFilter(mockRequest, mockResponse);
    }

    @Test
    public void shouldGetCorrectOriginFromSet() {
        Optional<String> expected = Optional.of("origin2");

        HttpSessionProperties mockHttpSessionProperties = mock(HttpSessionProperties.class);

        Set<String> origins = Sets.newHashSet(
                Arrays.asList(
                        "origin1",
                        expected.get()
                )
        );

        CorsFilter corsFilter = new CorsFilter(mockHttpSessionProperties);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        doReturn(expected.get())
                .when(mockRequest)
                .getRemoteHost();

        Optional<String> actual = corsFilter.accessControlAllowOrigins(mockRequest, origins);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnStarIfStarInSet() {
        String origin = "origin3";

        HttpSessionProperties mockHttpSessionProperties = mock(HttpSessionProperties.class);

        Set<String> origins = Sets.newHashSet(
                Arrays.asList(
                        "origin1",
                        "origin2",
                        "*"
                )
        );

        CorsFilter corsFilter = new CorsFilter(mockHttpSessionProperties);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        doReturn(origin)
                .when(mockRequest)
                .getRemoteAddr();

        Optional<String> expected = Optional.of("*");
        Optional<String> actual = corsFilter.accessControlAllowOrigins(mockRequest, origins);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnDelimitedListFromSet() {
        Set<String> methods = Sets.newHashSet(Arrays.asList("a", "b", "c"));

        HttpSessionProperties mockHttpSessionProperties = mock(HttpSessionProperties.class);

        CorsFilter corsFilter = new CorsFilter(mockHttpSessionProperties);

        String expected = "a, b, c";
        String actual = corsFilter.accessControlAllowFor(methods);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnStarFromSet() {
        Set<String> methods = Sets.newHashSet(Arrays.asList("a", "b", "c", "*"));

        HttpSessionProperties mockHttpSessionProperties = mock(HttpSessionProperties.class);

        CorsFilter corsFilter = new CorsFilter(mockHttpSessionProperties);

        String expected = "*";
        String actual = corsFilter.accessControlAllowFor(methods);

        assertEquals(expected, actual);
    }

}
