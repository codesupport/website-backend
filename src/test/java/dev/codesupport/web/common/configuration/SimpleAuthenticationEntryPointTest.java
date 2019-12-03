package dev.codesupport.web.common.configuration;

import org.junit.Test;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SimpleAuthenticationEntryPointTest {

    @Test
    public void shouldHaveSentCorrectErrorInResponse() throws IOException {
        HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockHttpServletResponse = mock(HttpServletResponse.class);
        AuthenticationException mockAuthenticationException = mock(AuthenticationException.class);

        SimpleAuthenticationEntryPoint entryPoint = new SimpleAuthenticationEntryPoint();

        doNothing()
                .when(mockHttpServletResponse)
                .sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

        entryPoint.commence(mockHttpServletRequest, mockHttpServletResponse, mockAuthenticationException);

        verify(mockHttpServletResponse, times(1))
                .sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
