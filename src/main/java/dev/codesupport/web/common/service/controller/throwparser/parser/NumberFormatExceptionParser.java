package dev.codesupport.web.common.service.controller.throwparser.parser;

import lombok.NoArgsConstructor;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import org.springframework.stereotype.Component;

/**
 * Used to parse {@link NumberFormatException} type throwables.
 *
 * @see NumberFormatException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
class NumberFormatExceptionParser extends AbstractThrowableParser<NumberFormatException> {

    @Override
    protected AbstractThrowableParser<NumberFormatException> instantiate() {
        return new NumberFormatExceptionParser();
    }

    @Override
    protected String responseMessage() {
        return throwable.getMessage();
    }
}
