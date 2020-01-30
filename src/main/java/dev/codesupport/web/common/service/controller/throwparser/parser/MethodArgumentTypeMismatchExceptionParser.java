package dev.codesupport.web.common.service.controller.throwparser.parser;

import lombok.NoArgsConstructor;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Used to parse Spring's {@link MethodArgumentTypeMismatchException} type throwables.
 *
 * @see MethodArgumentTypeMismatchException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class MethodArgumentTypeMismatchExceptionParser extends AbstractThrowableParser<MethodArgumentTypeMismatchException> {

    @Override
    protected AbstractThrowableParser<MethodArgumentTypeMismatchException> instantiate() {
        return new MethodArgumentTypeMismatchExceptionParser();
    }

    @Override
    protected String responseMessage() {
        return "Invalid parameter " + throwable.getName() + " (Required: " + throwable.getParameter().getParameterType().getSimpleName() + ")";
    }
}