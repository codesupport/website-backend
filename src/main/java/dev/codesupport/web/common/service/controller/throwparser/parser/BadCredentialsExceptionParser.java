package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

/**
 * Used to parse Spring's {@link BadCredentialsException} type throwables.
 *
 * @see BadCredentialsException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class BadCredentialsExceptionParser extends AbstractThrowableParser<BadCredentialsException> {

    @Override
    protected AbstractThrowableParser<BadCredentialsException> instantiate() {
        return new BadCredentialsExceptionParser();
    }

    @Override
    protected String responseMessage() {
        return "The username/password supplied was invalid/inactive";
    }

    @Override
    protected RestStatus responseStatus() {
        return RestStatus.UNAUTHORIZED;
    }
}
