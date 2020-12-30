package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.FileTooBigException;
import dev.codesupport.web.common.exception.MalformedDataException;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import dev.codesupport.web.common.service.service.RestStatus;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Used to parse {@link MalformedDataException} type throwables.
 *
 * @see MalformedDataException
 * @see AbstractThrowableParser
 */
@Component
@NoArgsConstructor
public class FileTooBigExceptionParser extends AbstractThrowableParser<FileTooBigException> {

    @Override
    protected AbstractThrowableParser<FileTooBigException> instantiate() {
        return new FileTooBigExceptionParser();
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
        return 413;
    }
}
