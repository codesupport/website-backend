package dev.codesupport.web.common.service.controller.throwparser;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * Default parser to be used when no other associated parser can be found.
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DefaultExceptionParser extends AbstractThrowableParser<Throwable> {

    @Override
    protected AbstractThrowableParser<Throwable> instantiate() {
        return new DefaultExceptionParser();
    }

    @Override
    protected List<String> responseMessage() {
        return Collections.singletonList("Server failure, exception logged.");
    }
}
