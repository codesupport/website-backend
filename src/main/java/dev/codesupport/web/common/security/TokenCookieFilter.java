package dev.codesupport.web.common.security;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.api.data.entity.PermissionEntity;
import dev.codesupport.web.api.data.entity.RoleEntity;
import dev.codesupport.web.api.data.entity.UserEntity;
import dev.codesupport.web.common.configuration.HttpSessionProperties;
import dev.codesupport.web.common.security.models.UserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles pulling AccessToken cookies from the request and adding them to the response as appropriate.
 */
@Component
@RequiredArgsConstructor
public class TokenCookieFilter extends OncePerRequestFilter {

    private final HttpSessionProperties httpSessionProperties;
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isNotAuthenticateEndpoint(request.getRequestURI())) {
            pullTokenCookieFromRequest(request)
                    .map(Cookie::getValue)
                    .ifPresent(token -> setContextForToken(request, response, token));
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Checks if the URI is the /authenticate endpoint, if it is, it's a login so don't make a cookie yet.
     *
     * @param uri The uri of the request
     * @return True if the URI is equal to /authenticate, False otherwise.
     */
    @VisibleForTesting
    boolean isNotAuthenticateEndpoint(String uri) {
        return !StringUtils.stripEnd(uri, "/").equals("/authenticate");
    }

    /**
     * Gets the AccessToken cookie from the request, if it exists.
     *
     * @param request The request to search for the cookie
     * @return An optional that may contain the cookie, if it was found.
     */
    @VisibleForTesting
    Optional<Cookie> pullTokenCookieFromRequest(HttpServletRequest request) {
        return Stream.of(ObjectUtils.defaultIfNull(request.getCookies(), new Cookie[0]))
                .filter(c -> httpSessionProperties.getCookie().getName().equals(c.getName()))
                .findFirst();
    }

    /**
     * <p>Gets the user associated with the cookie, and if the token isn't expired, sets the security context
     * as well as generates a new cookie and puts it in the Set-Cookie header of the response.</p>
     *
     * @param request  The associated request
     * @param response The associated response
     * @param token    The token that was pulled from the request
     */
    @VisibleForTesting
    void setContextForToken(HttpServletRequest request, HttpServletResponse response, String token) {
        UserEntity userEntity = authenticationService.getUserByToken(token);
        if (userEntity.getAccessTokenExpireOn() > System.currentTimeMillis()) {
            configureSpringSecurityContext(request, createUserDetails(userEntity));
            String value = authenticationService.createTokenCookieForUser(userEntity);
            response.setHeader(HttpHeaders.SET_COOKIE, value);
        }
    }

    /**
     * Configures the Spring Security Context
     * <p>The Spring Security Context is used later by the Accessors
     * to decide if you have the right to use a certain feature.</p>
     *
     * @param request     The requested associated to the configuration
     * @param userDetails The {@link UserDetails} to be configured in the security context
     * @see dev.codesupport.web.common.security.access.AbstractAccessEvaluator
     */
    @VisibleForTesting
    void configureSpringSecurityContext(HttpServletRequest request, UserDetails userDetails) {
        EmailPasswordAuthenticationToken emailPasswordAuthenticationToken = new EmailPasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        emailPasswordAuthenticationToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(emailPasswordAuthenticationToken);
    }

    /**
     * Creates {@link UserDetails}
     * <p>The {@link UserDetails} are used later for access control logic</p>
     *
     * @param userEntity The details to generate the {@link UserDetails} object from
     * @return The {@link UserDetails} object.
     */
    @VisibleForTesting
    UserDetails createUserDetails(UserEntity userEntity) {
        Set<String> permissions = userEntity.getPermission()
                .stream().map(PermissionEntity::getCode).collect(Collectors.toSet());
        permissions.addAll(
                ObjectUtils.defaultIfNull(userEntity.getRole(), new RoleEntity()).getPermission()
                        .stream().map(PermissionEntity::getCode).collect(Collectors.toSet())
        );

        return new UserDetails(
                userEntity.getId(),
                userEntity.getAlias(),
                userEntity.getHashPassword(),
                userEntity.getEmail(),
                permissions,
                userEntity.isDisabled(),
                StringUtils.isBlank(userEntity.getVerifyToken())
        );
    }

}

