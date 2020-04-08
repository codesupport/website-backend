package dev.codesupport.web.common.service.controller.throwparser.parser;

import lombok.NoArgsConstructor;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Used to parse Spring's {@link HttpMessageNotReadableException} type throwables.
 *
 * @see HttpMessageNotReadableException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class HttpMessageNotReadableExceptionParser extends AbstractThrowableParser<HttpMessageNotReadableException> {

    @Override
    protected AbstractThrowableParser<HttpMessageNotReadableException> instantiate() {
        return new HttpMessageNotReadableExceptionParser();
    }

    @Override
    protected List<String> responseMessage() {
        return Collections.singletonList("Could not parse JSON payload.");
    }
}
