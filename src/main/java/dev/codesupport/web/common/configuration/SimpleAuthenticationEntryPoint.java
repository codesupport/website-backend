package dev.codesupport.web.common.configuration;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Overrides response of unauthorized requests, sending back 401 status
 */
@Component
public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Implements modifications of request/response for authentications
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}