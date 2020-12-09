package dev.codesupport.web.common.service.controller.throwparser.parser;

import com.google.common.collect.Lists;
import dev.codesupport.web.common.service.controller.throwparser.AbstractThrowableParser;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Arrays;
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
    protected List<String> responseMessage() {
        return Arrays.stream(throwable.getConstraintViolations().toArray(new ConstraintViolation[]{}))
                .map(constraintViolation -> {
                    List<Path.Node> nodes = Lists.newArrayList(constraintViolation.getPropertyPath().iterator());

                    return nodes.get(nodes.size() - 1) + ": " + constraintViolation.getMessage();
                }).collect(Collectors.toList());
    }

    @Override
    public int responseCode() {
        return 400;
    }
}
