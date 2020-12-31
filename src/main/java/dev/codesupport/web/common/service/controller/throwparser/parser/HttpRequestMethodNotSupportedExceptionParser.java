package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Collections;
import java.util.List;

/**
 * Used to parse Spring's {@link HttpRequestMethodNotSupportedException} type throwables.
 *
 * @see HttpRequestMethodNotSupportedException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class HttpRequestMethodNotSupportedExceptionParser extends AbstractThrowableParser<HttpRequestMethodNotSupportedException> {

    @Override
    protected AbstractThrowableParser<HttpRequestMethodNotSupportedException> instantiate() {
        return new HttpRequestMethodNotSupportedExceptionParser();
    }

    @Override
    protected List<String> responseMessage() {
        return Collections.singletonList("Request method not supported for this endpoint.");
    }

    @Override
    public int responseCode() {
        return 405;
    }
}
