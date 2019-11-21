package dev.codesupport.web.common.service.controller.throwparser;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import dev.codesupport.web.common.service.service.RestResponse;
import dev.codesupport.web.common.service.service.RestStatus;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
public abstract class AbstractThrowableParser<E extends Throwable> {

    protected E throwable;

    protected abstract AbstractThrowableParser<E> instantiate();

    AbstractThrowableParser<E> instantiate(E throwable){
        AbstractThrowableParser<E> throwableParser = instantiate();
        throwableParser.throwable = throwable;
        return throwableParser;
    }

    public <T extends Serializable> void modifyResponse(RestResponse<T> restResponse){
        restResponse.setStatus(responseStatus());
        restResponse.setMessage(responseMessage());
    }

    protected abstract String responseMessage();

    protected RestStatus responseStatus(){
        return RestStatus.FAIL;
    }

}
