package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Used to parse Spring's {@link InvalidUserException} type throwables.
 *
 * @see InvalidUserException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class InvalidUserExceptionParser extends AbstractThrowableParser<InvalidUserException> {

    @Override
    protected AbstractThrowableParser<InvalidUserException> instantiate() {
        return new InvalidUserExceptionParser();
    }

    @Override
    protected List<String> responseMessage() {
        return Collections.singletonList(throwable.getMessage());
    }

    @Override
    protected RestStatus responseStatus() {
        return RestStatus.UNAUTHORIZED;
    }
}
