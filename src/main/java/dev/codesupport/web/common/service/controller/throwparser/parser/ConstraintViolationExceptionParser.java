package dev.codesupport.web.common.service.controller.throwparser.parser;

import com.google.common.collect.Lists;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used to parse Java's {@link ConstraintViolationException} type throwables.
 *
 * @see ConstraintViolationException
 * @see AbstractThrowableParser
 */
@Component
@EqualsAndHashCode(callSuper = true)
public class ConstraintViolationExceptionParser extends AbstractThrowableParser<ConstraintViolationException> {

    @Override
    protected AbstractThrowableParser<ConstraintViolationException> instantiate() {
        return new ConstraintViolationExceptionParser();
    }

    @Override
    protected String responseMessage() {
        List<ConstraintViolation<?>> brokenConstraints = Lists.newArrayList(
                throwable.getConstraintViolations().iterator()
        );

        String brokenConstraintMessage = brokenConstraints.stream().map(e -> {
            List<Path.Node> pathNodes = Lists.newArrayList(e.getPropertyPath().iterator());
            return "[" + pathNodes.get(pathNodes.size() - 1).toString() + " '" + e.getInvalidValue() + "': " + e.getMessage() + "]";
        }).collect(Collectors.joining(","));

        return "Invalid parameter(s): " + brokenConstraintMessage;

    }
}
