package dev.codesupport.web.common.service.http;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode
@ToString
abstract class RestRequest<S extends Serializable, T extends Serializable> {

    protected String url;
    protected Map<String, String> headers;
    protected Map<String, String> parameters;
    protected S requestObject;
    protected Class<T> responseClass;
    protected Class<S> requestClass;

    RestRequest() {
        this.headers = new HashMap<>();
        this.parameters = new HashMap<>();
        this.headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
    }

    RestRequest(Class<S> requestClass, Class<T> responseClass) {
        this();
        this.requestClass = requestClass;
        this.responseClass = responseClass;
    }

    public RestRequest<S, T> withUrl(String url) {
        this.url = url;
        return this;
    }

    public RestRequest<S, T> withParameter(String parameter, String value) {
        parameters.put(parameter, value);
        return this;
    }

    public RestRequest<S, T> withParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
        return this;
    }

    public RestRequest<S, T> withHeader(String header, String value) {
        headers.put(header, value);
        return this;
    }

    public RestRequest<S, T> withHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public RestRequest<S, T> withPayload(S requestObject) {
        this.requestObject = requestObject;
        return this;
    }

    /**
     * Make a synchronous request
     *
     * @param httpMethod The http method of the request .
     * @return An object representing the response of the request.
     */
    public abstract T sync(HttpMethod httpMethod);

}
