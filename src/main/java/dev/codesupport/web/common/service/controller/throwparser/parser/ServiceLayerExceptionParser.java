package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.ServiceLayerException;
import lombok.NoArgsConstructor;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import org.springframework.stereotype.Component;

/**
 * Used to validate {@link ServiceLayerException} type throwables.
 *
 * @see ServiceLayerException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
class ServiceLayerExceptionParser extends AbstractThrowableParser<ServiceLayerException> {

    @Override
    protected AbstractThrowableParser<ServiceLayerException> instantiate() {
        return new ServiceLayerExceptionParser();
    }

    @Override
    protected String responseMessage() {
        return throwable.getMessage();
    }
}
