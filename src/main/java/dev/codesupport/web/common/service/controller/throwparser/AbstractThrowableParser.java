package dev.codesupport.web.common.service.controller.throwparser;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.common.service.service.RestStatus;

import java.io.Serializable;

/**
 * The abstract class extended when creating throwable (exception) parsers.
 *
 * @param <E> Throwable (Exception) class associated with the parser.
 */
@EqualsAndHashCode
@NoArgsConstructor
public abstract class AbstractThrowableParser<E extends Throwable> {

    protected E throwable;

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
    AbstractThrowableParser<E> instantiate(E throwable){
        AbstractThrowableParser<E> throwableParser = instantiate();
        throwableParser.throwable = throwable;
        return throwableParser;
    }

    /**
     * Modifies the given {@link RestResponse} with data generated from parsing the throwable.
     *
     * @param restResponse The response to modify with the parser data.
     * @param <T> The serializable type of the resource related to the response.
     */
    public <T extends Serializable> void modifyResponse(RestResponse<T> restResponse){
        restResponse.setStatus(responseStatus());
        restResponse.setMessage(responseMessage());
    }

    /**
     * Returns the message associated to the throwable.
     *
     * @return The message associated to the throwable.
     */
    protected abstract String responseMessage();

    /**
     * Returns the {@link RestStatus} associated to the throwable.
     *
     * @return The {@link RestStatus} associated to the throwable.
     */
    protected RestStatus responseStatus(){
        return RestStatus.FAIL;
    }

}
