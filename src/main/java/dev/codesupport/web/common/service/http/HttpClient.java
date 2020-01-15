package dev.codesupport.web.common.service.http;

import java.io.Serializable;

/**
 * Interface defining the capabilities of th client
 */
public interface HttpClient {

    <S extends Serializable, T extends Serializable> RestRequest<S, T> rest(Class<S> requestClass, Class<T> responseClass);

}
