package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.StaleDataException;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Used to parse {@link StaleDataException} type throwables.
 *
 * @see StaleDataException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class StaleDataExceptionParser extends AbstractThrowableParser<StaleDataException> {

    @Override
    protected AbstractThrowableParser<StaleDataException> instantiate() {
        return new StaleDataExceptionParser();
    }

    @Override
    protected List<String> responseMessage() {
        return Collections.singletonList("Resource contains stale data, please re-fetch data and try again.");
    }

    @Override
    protected RestStatus responseStatus() {
        return RestStatus.FAIL;
    }

    @Override
    public int responseCode() {
        return 422;
    }
}
