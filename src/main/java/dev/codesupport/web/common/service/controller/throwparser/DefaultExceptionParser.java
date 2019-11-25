package dev.codesupport.web.common.service.controller.throwparser;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Default parser to be used when no other associated parser can be found.
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
class DefaultExceptionParser extends AbstractThrowableParser<Throwable> {

    @Override
    protected AbstractThrowableParser<Throwable> instantiate() {
        return new DefaultExceptionParser();
    }

    @Override
    protected String responseMessage() {
        return "Server failure, exception logged.";
    }
}
