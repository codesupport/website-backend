package dev.codesupport.web.common.service.http.client;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Performs logic specifically for REST type http requests, used in conjunction with Http Client.
 * <p>Using this class to isolate properties for each request being made.</p>
 *
 * @param <S> The class type of the request object
 * @param <T> The class type of the response object
 */
@EqualsAndHashCode
@ToString
public abstract class RestRequest<S extends Serializable, T extends Serializable> {

    protected String url;
    protected Map<String, String> headers;
    protected Map<String, String> parameters;
    protected S requestObject;
    protected Class<T> responseClass;
    protected Class<S> requestClass;

    public RestRequest() {
        this.headers = new HashMap<>();
        this.parameters = new HashMap<>();
        // Set default User-Agent
        this.headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
    }

    public RestRequest(Class<S> requestClass, Class<T> responseClass) {
        this();
        this.requestClass = requestClass;
        this.responseClass = responseClass;
    }

    /**
     * Add url to be used in request
     *
     * @param url Url to be used in request
     * @return The class instance (builder pattern)
     */
    public RestRequest<S, T> withUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Add parameter to be used in request
     *
     * @param parameter Parameter name
     * @param value     Parameter value
     * @return The class instance (builder pattern)
     */
    public RestRequest<S, T> withParameter(String parameter, String value) {
        parameters.put(parameter, value);
        return this;
    }

    /**
     * Add map of parameters to be used in request
     *
     * @param parameters Map of parameters to add
     * @return The class instance (builder pattern)
     */
    public RestRequest<S, T> withParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
        return this;
    }

    /**
     * Add header to be used in request
     *
     * @param header Header name
     * @param value  Header value
     * @return The class instance (builder pattern)
     */
    public RestRequest<S, T> withHeader(String header, String value) {
        headers.put(header, value);
        return this;
    }

    /**
     * Add map of headers to be used in request
     *
     * @param headers Map of headers to add
     * @return The class instance (builder pattern)
     */
    public RestRequest<S, T> withHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    /**
     * Add an object to be added as the payload of the request
     *
     * @param requestObject Object to be added to payload
     * @return The class instance (builder pattern)
     */
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
