package dev.codesupport.web.common.service.controller.throwparser.parser;

import lombok.NoArgsConstructor;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import org.springframework.stereotype.Component;

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
