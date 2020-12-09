package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.DuplicateEntryException;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Used to parse {@link DuplicateEntryException} type throwables.
 *
 * @see DuplicateEntryException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class DuplicateEntryExceptionParser extends AbstractThrowableParser<DuplicateEntryException> {

    @Override
    protected AbstractThrowableParser<DuplicateEntryException> instantiate() {
        return new DuplicateEntryExceptionParser();
    }

    @Override
    protected List<String> responseMessage() {
        return Collections.singletonList(throwable.getMessage());
    }

    @Override
    protected RestStatus responseStatus() {
        return RestStatus.FAIL;
    }

    @Override
    public int responseCode() {
        return 409;
    }
}
