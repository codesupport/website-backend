package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;

/**
 * Used to parse Spring's {@link HttpMediaTypeNotSupportedException} type throwables.
 *
 * @see HttpMediaTypeNotSupportedException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
class HttpMediaTypeNotSupportedExceptionParser extends AbstractThrowableParser<HttpMediaTypeNotSupportedException> {

    @Override
    protected AbstractThrowableParser<HttpMediaTypeNotSupportedException> instantiate() {
        return new HttpMediaTypeNotSupportedExceptionParser();
    }

    @Override
    protected String responseMessage() {
        MediaType contentType = throwable.getContentType();
        String message = (contentType != null) ? contentType.getType() + "/" + contentType.getSubtype() : "";
        return "Content type not supported: " + message;
    }
}
