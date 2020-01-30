package dev.codesupport.web.common.service.controller.throwparser;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Responsible for creating parsers for given exceptions.
 */
@Component
public class ThrowableParserFactory {

    /**
     * Map of parsers as returned by Spring's ApplicationContext.
     */
    // This should be fine for the purpose here.
    @SuppressWarnings("rawtypes")
    private final Map<String, AbstractThrowableParser> parserMap;

    @Autowired
    public ThrowableParserFactory(ApplicationContext context) {
        // This should be fine for the purpose here.
        //noinspection rawtypes
        Map<String, AbstractThrowableParser> beanMap = context.getBeansOfType(AbstractThrowableParser.class);
        // Remap to key = canonical class name, value = parser object
        parserMap = beanMap.values().stream().collect(Collectors.toMap(AbstractThrowableParser::getThrowableClassType, Function.identity()));
    }

    /**
     * Creates an appropriate parser for the given exception.
     * <p>Searches it's list of parsers as provided by Spring's ApplicationContext and returns a new instance of
     * the parser, or returns the {@link DefaultExceptionParser} if no other appropriate parser can be found.</p>
     *
     * @param throwable The exception to be parsed.
     * @return A parser to be used for parsing the exception message.
     * @throws NullPointerException If the provided throwable was null.
     */
    // This should be fine for the purpose here.
    @SuppressWarnings("rawtypes")
    public AbstractThrowableParser createParser(Throwable throwable) {
        Objects.requireNonNull(throwable);

        Throwable rootCause = getRootCause(throwable);

        String parserName = getParserNameFromException(rootCause);

        // This should be fine for the purpose here.
        //noinspection rawtypes
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

    /**
     * Finds either the first parsable exception, or the root cause of the exception.
     * <p>If the exception has an associated parser, that exception will be returned, else it will recursively
     * look for the cause of that exception until it either finds the root cause, or a parsable exception.</p>
     *
     * @param throwable The throwable to be searched.
     * @return The first parsable throwable or root cause.
     */
    @VisibleForTesting
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

    /**
     * Generates the convention driven map key of the parser associated to the given throwable.
     *
     * @param throwable The throwable to get the parser name for.
     * @return The map key of the parser that would be associated to the given throwable.
     */
    @VisibleForTesting
    String getParserNameFromException(Throwable throwable) {
        return throwable.getClass().getCanonicalName();
    }

}
