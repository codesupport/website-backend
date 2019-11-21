package dev.codesupport.web.common.service.controller.throwparser;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
