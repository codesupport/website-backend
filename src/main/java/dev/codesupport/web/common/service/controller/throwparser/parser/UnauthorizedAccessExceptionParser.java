package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.UnauthorizedAccessException;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Used to parse Spring's {@link UnauthorizedAccessException} type throwables.
 *
 * @see UnauthorizedAccessException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class UnauthorizedAccessExceptionParser extends AbstractThrowableParser<UnauthorizedAccessException> {

    @Override
    protected AbstractThrowableParser<UnauthorizedAccessException> instantiate() {
        return new UnauthorizedAccessExceptionParser();
    }

    @Override
    protected List<String> responseMessage() {
        return Collections.singletonList(throwable.getMessage());
    }

    @Override
    protected RestStatus responseStatus() {
        return RestStatus.UNAUTHORIZED;
    }

    @Override
    public int responseCode() {
        return 401;
    }
}
