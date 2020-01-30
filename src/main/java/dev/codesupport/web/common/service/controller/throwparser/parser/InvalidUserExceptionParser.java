package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.InvalidUserException;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

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
    protected String responseMessage() {
        return throwable.getMessage();
    }

}
