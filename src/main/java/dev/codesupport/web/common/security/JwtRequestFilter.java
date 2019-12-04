package dev.codesupport.web.common.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.codesupport.web.common.exception.InvalidTokenException;
import dev.codesupport.web.common.service.service.AuthenticationUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Intercepts all incoming requests, checking for and validating any present JWT
 */
@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private AuthenticationUserDetailsService userDetailsService;
    private JsonWebTokenFactory jsonWebTokenFactory;

    @Autowired
    public JwtRequestFilter(
            AuthenticationUserDetailsService userDetailsService,
            JsonWebTokenFactory jsonWebTokenFactory
    ) {
        this.userDetailsService = userDetailsService;
        this.jsonWebTokenFactory = jsonWebTokenFactory;
    }

    /**
     * Executes the implemented process for the request.
     * This is called automatically by the framework.
     *
     * @param request  The associated request
     * @param response The associated response
     * @param chain    The filter chain
     * @throws ServletException Thrown by framework
     * @throws IOException      Thrown by framework
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        checkForJWToken(request);

        // Boilerplate call, invokes subsequent filter layers
        chain.doFilter(request, response);
    }

    /**
     * Checks for available tokens and configures security context
     * <p>If no token is found, security context is not configured, meaning request will only be
     * able to access non-authenticated endpoints.</p>
     *
     * @param request The corresponding request
     */
    void checkForJWToken(HttpServletRequest request) {
        String tokenString = getTokenFromRequest(request);
        // If token is empty, skip all this, could be an authentication post request.
        if (!StringUtils.isEmpty(tokenString)) {
            validateJWToken(tokenString, request);
        }
    }

    /**
     * Validate token and configure Spring Security Context
     *
     * @param tokenString The token to validate
     * @param request     The associated request
     */
    void validateJWToken(String tokenString, HttpServletRequest request) {
        Assert.notNull(tokenString, "tokenString can not be null");
        try {
            // Get token from request, throws InvalidTokenException if invalid
            JsonWebToken jwToken = jsonWebTokenFactory.createToken(tokenString);

            String username = jwToken.getUsername();

            configureSpringSecurityContext(username, request);
        } catch (InvalidTokenException e) {
            // Doesn't matter, just log it for debugging and the service with return an unauthorized error.
            if (log.isDebugEnabled()) {
                log.debug("Failed to validate token", e);
            }
        }
    }

    /**
     * Configures the Spring Security Context
     * <p>If not already configured, this method configures the Spring Security Context, which is
     * used later by the framework for access checks, etc.</p>
     *
     * @param username The user to be configured in the security context
     * @param request  The requested associated to the configuration
     */
    void configureSpringSecurityContext(String username, HttpServletRequest request) {
        Assert.notNull(username, "username can not be null");

        // If authentication is not already configured
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

    /**
     * Extract token from header.
     *
     * @param request Corresponding request to extract token from
     * @return The raw token ascii string
     */
    String getTokenFromRequest(HttpServletRequest request) {
        final String requestAuthorizationHeader = request.getHeader("Authorization");
        String jwtToken;

        // Get the token from the header
        if (!StringUtils.isEmpty(requestAuthorizationHeader)) {
            if (StringUtils.startsWith(requestAuthorizationHeader, "Bearer ")) {
                jwtToken = requestAuthorizationHeader.substring(7); //Removes "Bearer " prefix
            } else { // Invalid authorization header
                jwtToken = null;
                if (log.isDebugEnabled()) {
                    logger.debug("Authorization header does not begin with Bearer ");
                }
            }
        } else { // Missing authorization header
            jwtToken = null;
            if (log.isDebugEnabled()) {
                logger.debug("Authorization header value is missing.");
            }
        }

        return jwtToken;
    }

}