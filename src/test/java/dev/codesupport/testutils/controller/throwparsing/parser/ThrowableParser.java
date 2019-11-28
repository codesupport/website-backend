package dev.codesupport.testutils.controller.throwparsing.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;

public class ThrowableParser extends AbstractThrowableParser<Throwable> {

    @Override
    protected AbstractThrowableParser<Throwable> instantiate() {
        return new ThrowableParser();
    }

    @Override
    protected String responseMessage() {
        return "Mock parser message";
    }

    @Override
    protected RestStatus responseStatus() {
        return RestStatus.WARNING;
    }

}
