package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * Used to parse Spring's {@link AccessDeniedException} type throwables.
 *
 * @see AccessDeniedException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class AccessDeniedExceptionParser extends AbstractThrowableParser<AccessDeniedException> {

    @Override
    protected AbstractThrowableParser<AccessDeniedException> instantiate() {
        return new AccessDeniedExceptionParser();
    }

    @Override
    protected String responseMessage() {
        return "You are not permitted to perform the requested action on the requested resource.";
    }

    @Override
    protected RestStatus responseStatus() {
        return RestStatus.UNAUTHORIZED;
    }
}
