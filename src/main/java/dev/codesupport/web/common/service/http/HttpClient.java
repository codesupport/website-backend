package dev.codesupport.web.common.service.http;

import java.io.Serializable;

public interface HttpClient {

    <S extends Serializable, T extends Serializable> RestRequest<S, T> rest(Class<S> requestClass, Class<T> responseClass);

}
