package dev.codesupport.web.common.security;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.common.configuration.HttpSessionProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

/**
 * Handles setting up
 */
@Component
@RequiredArgsConstructor
public class CorsFilter extends OncePerRequestFilter {

    private final HttpSessionProperties httpSessionProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        accessControlAllowOrigins(request, httpSessionProperties.getCors().getOrigins())
            .ifPresent(origin -> {
                response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
                response.setHeader(
                        HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                        accessControlAllowMethods(httpSessionProperties.getCors().getMethods())
                );
            });

        filterChain.doFilter(request, response);
    }

    @VisibleForTesting
    Optional<String> accessControlAllowOrigins(HttpServletRequest request, Set<String> origins) {
        Optional<String> origin;

        if (origins.stream().anyMatch(o -> o.equals("*"))) {
            origin = Optional.of("*");
        } else if (origins.contains(request.getRemoteHost())) {
            origin = Optional.of(request.getRemoteHost());
        } else {
            origin = Optional.empty();
        }

        return origin;
    }

    @VisibleForTesting
    String accessControlAllowMethods(Set<String> methods) {
        String method;

        if (methods.stream().anyMatch(m -> m.equals("*"))) {
            method = "*";
        } else {
            method = StringUtils.join(methods, ", ");
        }

        return method;
    }

}

