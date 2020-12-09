package dev.codesupport.web.common.service.controller.throwparser;

import com.google.common.annotations.VisibleForTesting;
import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.common.service.service.RestStatus;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * The abstract class extended when creating throwable (exception) parsers.
 *
 * @param <E> Throwable (Exception) class associated with the parser.
 */
@EqualsAndHashCode
public abstract class AbstractThrowableParser<E extends Throwable> {

    protected E throwable;
    private final Class<E> throwableClassType;

    public AbstractThrowableParser() {
        // Finds the type of the class parameter and stores it for later mappings.
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            Type parameterizedType = ((ParameterizedType) superClass).getActualTypeArguments()[0];
            if (parameterizedType instanceof Class) {
                //unchecked - This should be all set.
                //noinspection unchecked
                throwableClassType = (Class<E>) parameterizedType;
            } else {
                // This is really not possible.
                throw new IllegalArgumentException("Internal error: Parameter was not a class.");
            }
        } else {
            // Must not have a class parameter, throw an exception.
            throw new IllegalArgumentException("Internal error: Cannot instantiate AbstractThrowableParser without exception type.");
        }
    }

    /**
     * Gets the class type of the throwable associated to the parser.
     *
     * @return The string value of the canonical name for the class
     */
    public String getThrowableClassType() {
        return throwableClassType.getCanonicalName();
    }

    /**
     * Creates a new instance of the parser.
     *
     * @return New instance of the parser.
     */
    protected abstract AbstractThrowableParser<E> instantiate();

    /**
     * Creates an Instance of the parser with the given throwable.
     *
     * @param throwable The throwable to be used in the parser.
     * @return Instance of the parser with the given throwable added.
     */
    @VisibleForTesting
    AbstractThrowableParser<E> instantiate(E throwable) {
        AbstractThrowableParser<E> throwableParser = instantiate();
        throwableParser.throwable = throwable;
        return throwableParser;
    }

    /**
     * Modifies the given {@link RestResponse} with data generated from parsing the throwable.
     *
     * @param restResponse The response to modify with the parser data.
     * @param <T>          The serializable type of the resource related to the response.
     */
    public <T extends Serializable> void modifyResponse(RestResponse<T> restResponse) {
        restResponse.setStatus(responseStatus());
        restResponse.setMessage(responseMessage());
    }

    /**
     * Returns the message associated to the throwable.
     *
     * @return The message associated to the throwable.
     */
    protected abstract List<String> responseMessage();

    /**
     * Returns the {@link RestStatus} associated to the throwable.
     *
     * @return The {@link RestStatus} associated to the throwable.
     */
    protected RestStatus responseStatus() {
        return RestStatus.FAIL;
    }

    public int responseCode() { return 500; }

}
