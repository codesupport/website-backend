package dev.codesupport.testutils.controller.throwparsing.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class ThrowableParser extends AbstractThrowableParser<Throwable> {

    @Override
    protected AbstractThrowableParser<Throwable> instantiate() {
        return new ThrowableParser();
    }

    @Override
    protected List<String> responseMessage() {
        return Collections.singletonList("Mock parser message");
    }

    @Override
    protected RestStatus responseStatus() {
        return RestStatus.WARNING;
    }

}
