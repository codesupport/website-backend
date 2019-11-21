package dev.codesupport.web.common.service.controller.throwparser;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class ThrowableParserFactory {

    private final Map<String, AbstractThrowableParser> parserMap;

    @Autowired
    ThrowableParserFactory(ApplicationContext context) {
        parserMap = context.getBeansOfType(AbstractThrowableParser.class);
    }

    public AbstractThrowableParser createParser(Throwable throwable) {
        Objects.requireNonNull(throwable);

        Throwable rootCause = getRootCause(throwable);

        String parserName = getParserNameFromException(rootCause);

        AbstractThrowableParser parser;

        if (parserMap.containsKey(parserName)) {
            //This is a safe cast but would be better if it could be done without being suppressed.
            //noinspection unchecked
            parser = parserMap.get(parserName).instantiate(rootCause);
        } else {
            parser = new DefaultExceptionParser();
        }

        return parser;
    }

    Throwable getRootCause(Throwable throwable) {
        Throwable rootCause = throwable;

        while (
                rootCause.getCause() != null &&
                        !parserMap.containsKey(getParserNameFromException(rootCause)) &&
                        rootCause.getCause() != rootCause
        ) {
            rootCause = rootCause.getCause();
        }

        return rootCause;
    }

    String getParserNameFromException(Throwable throwable) {
        return StringUtils.uncapitalize(throwable.getClass().getSimpleName() + "Parser");
    }

}
