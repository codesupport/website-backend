package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.ResourceNotFoundException;
import lombok.NoArgsConstructor;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Used to parse {@link ResourceNotFoundException} type throwables.
 *
 * @see ResourceNotFoundException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class ResourceNotFoundExceptionParser extends AbstractThrowableParser<ResourceNotFoundException> {

    @Override
    protected AbstractThrowableParser<ResourceNotFoundException> instantiate() {
        return new ResourceNotFoundExceptionParser();
    }

    @Override
    protected List<String> responseMessage() {
        return Collections.singletonList(throwable.getMessage());
    }

    @Override
    protected RestStatus responseStatus() {
        return RestStatus.NOT_FOUND;
    }
}
