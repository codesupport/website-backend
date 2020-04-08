package dev.codesupport.web.common.service.controller.throwparser.parser;

import lombok.NoArgsConstructor;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Used to parse {@link NumberFormatException} type throwables.
 *
 * @see NumberFormatException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class NumberFormatExceptionParser extends AbstractThrowableParser<NumberFormatException> {

    @Override
    protected AbstractThrowableParser<NumberFormatException> instantiate() {
        return new NumberFormatExceptionParser();
    }

    @Override
    protected List<String> responseMessage() {
        return Collections.singletonList(throwable.getMessage());
    }
}
