package dev.codesupport.web.common.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.codesupport.web.common.service.service.AuthenticationUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Intercepts all incoming requests, checking for and validating any present JWT
 */
@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private AuthenticationUserDetailsService userDetailsService;

    @Autowired
    public JwtRequestFilter(AuthenticationUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Executes the implemented process for the request.
     * This is called automatically by the framework.
     *
     * @param request  The associated request
     * @param response The associated response
     * @param chain    The filter chain
     * @throws ServletException Thrown by framework
     * @throws IOException Thrown by framework
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Configure the spring security context
        configureSpringSecurityContext(request);

        // Boilerplate call, invokes subsequent filter layers
        chain.doFilter(request, response);
    }

    /**
     * Configures the Spring security context for the request
     *
     * @param request The corresponding request
     */
    void configureSpringSecurityContext(HttpServletRequest request) {
        // Get token from request
        String jwtToken = getTokenFromRequest(request);

        if (jwtToken != null) {
            //TODO: Get username from token, this is a stub.
            String username = "admin"; // stub

            // If username found and authentication is not already configured
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Get user's details
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                //TODO: Validate token correctly, this is a stub.
                if (jwtToken.equalsIgnoreCase("1234")) { // Stub
                    // Configure the authentication so it can be used later by the framework for access checks.
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
    }

    /**
     * Extract token from header.
     *
     * @param request Corresponding request to extract token from
     * @return The raw token ascii string
     */
    String getTokenFromRequest(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken;

        // Get the token from the header
        if (StringUtils.startsWith(requestTokenHeader, "Bearer ")) {
            jwtToken = requestTokenHeader.substring(7); //Removes "Bearer " prefix
        } else { // Invalid authorization header
            jwtToken = null;
            if (log.isDebugEnabled()) {
                logger.debug("JWT Token does not begin with Bearer String");
            }
        }

        return jwtToken;
    }
}