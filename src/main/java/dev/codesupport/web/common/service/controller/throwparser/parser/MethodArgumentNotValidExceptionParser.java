package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import lombok.NoArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;

/**
 * Used to parse Spring's {@link MethodArgumentNotValidException} type throwables.
 *
 * @see MethodArgumentNotValidException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class MethodArgumentNotValidExceptionParser extends AbstractThrowableParser<MethodArgumentNotValidException> {

    @Override
    protected AbstractThrowableParser<MethodArgumentNotValidException> instantiate() {
        return new MethodArgumentNotValidExceptionParser();
    }

    @Override
    protected String responseMessage() {
        return throwable
                .getBindingResult()
                .getFieldErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
    }
}