package dev.codesupport.web.common.service.controller.throwparser.parser;

import dev.codesupport.web.common.exception.ValidationException;
import lombok.NoArgsConstructor;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@NoArgsConstructor
class ValidationExceptionParser extends AbstractThrowableParser<ValidationException> {

    @Override
    protected AbstractThrowableParser<ValidationException> instantiate() {
        return new ValidationExceptionParser();
    }

    @Override
    protected String responseMessage() {
        return throwable.getValidationIssues().stream()
                .map(e -> "[" + e.getParameterName() + ": " + e.getMessage() + "]")
                .collect(Collectors.joining(","));
    }
}
