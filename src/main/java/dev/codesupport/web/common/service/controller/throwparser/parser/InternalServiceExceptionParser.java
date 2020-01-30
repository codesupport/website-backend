package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.InternalServiceException;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Used to validate {@link InternalServiceException} type throwables.
 *
 * @see InternalServiceException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class InternalServiceExceptionParser extends AbstractThrowableParser<InternalServiceException> {

    @Override
    protected AbstractThrowableParser<InternalServiceException> instantiate() {
        return new InternalServiceExceptionParser();
    }

    @Override
    protected String responseMessage() {
        return throwable.getMessage();
    }
}
