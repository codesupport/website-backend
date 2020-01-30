package dev.codesupport.web.common.security;

import dev.codesupport.web.common.exception.InvalidTokenException;
import dev.codesupport.web.common.security.jwt.JsonWebToken;
import dev.codesupport.web.common.security.jwt.JsonWebTokenFactory;
import dev.codesupport.web.common.security.models.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Intercepts all incoming requests, checking for and validating any present JWT
 */
@Component
@Slf4j
public class HttpRequestFilter extends OncePerRequestFilter {

    private final JsonWebTokenFactory jsonWebTokenFactory;
    private final AuthorizationService authorizationService;

    @Autowired
    public HttpRequestFilter(
            JsonWebTokenFactory jsonWebTokenFactory,
            AuthorizationService authorizationService
    ) {
        this.jsonWebTokenFactory = jsonWebTokenFactory;
        this.authorizationService = authorizationService;
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

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

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

            String email = jwToken.getEmail();

            configureSpringSecurityContext(email, request);
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
     * @param email   The user to be configured in the security context
     * @param request The requested associated to the configuration
     */
    void configureSpringSecurityContext(String email, HttpServletRequest request) {
        Assert.notNull(email, "email can not be null");

        // If authentication is not already configured
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = authorizationService.getUserDetailsByEmail(email);

            EmailPasswordAuthenticationToken emailPasswordAuthenticationToken = new EmailPasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            emailPasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(emailPasswordAuthenticationToken);
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