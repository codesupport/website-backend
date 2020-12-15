package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import lombok.NoArgsConstructor;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Used to parse Spring's {@link RequestRejectedException} type throwables.
 *
 * @see RequestRejectedException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class RequestRejectedExceptionParser extends AbstractThrowableParser<RequestRejectedException> {

    @Override
    protected AbstractThrowableParser<RequestRejectedException> instantiate() {
        return new RequestRejectedExceptionParser();
    }

    @Override
    protected List<String> responseMessage() {
        return Collections.singletonList("The requested resource does not exist.");
    }

    @Override
    protected RestStatus responseStatus() {
        return RestStatus.NOT_FOUND;
    }

    @Override
    public int responseCode() {
        return 404;
    }
}
