package dev.codesupport.web.common.service.http.client;

import dev.codesupport.web.common.exception.HttpRequestException;
import dev.codesupport.web.common.exception.InternalServiceException;
import dev.codesupport.web.common.util.MappingUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Spring implementation of the {@link HttpClient}
 * @see HttpClient
 */
@Component
@EqualsAndHashCode
public class SpringHttpClient implements HttpClient {

    // Using Spring's RestTemplate http client
    private RestTemplate restTemplate;

    @Autowired
    public SpringHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Making a REST based http request
     *
     * @param requestClass The request payload class type
     * @param responseClass The response payload class type
     * @param <S> The generic type for the request payload
     * @param <T> The generic type for the response payload
     * @return A new instance of the Spring implementation of the RestRequest.
     */
    @Override
    public <S extends Serializable, T extends Serializable> RestRequest<S, T> rest(Class<S> requestClass, Class<T> responseClass) {
        return new SpringRestRequest<>(restTemplate, requestClass, responseClass);
    }

    /**
     * Spring implementation of the RestRequest class
     * <p>This allows isolation of the properties set for each http request made with the HttpClient.</p>
     *
     * @param <S> The generic type of the request payload
     * @param <T> The generic type of the response payload
     */
    @EqualsAndHashCode(callSuper = true)
    @ToString
    public static class SpringRestRequest<S extends Serializable, T extends Serializable> extends RestRequest<S, T> {

        @ToString.Exclude
        private RestTemplate restTemplate;

        SpringRestRequest(RestTemplate restTemplate, Class<S> requestClass, Class<T> responseClass) {
            super(requestClass, responseClass);
            this.restTemplate = restTemplate;
        }

        /**
         * Implementation of the synchronous (sync) request.
         *
         * @param httpMethod The http method of the request.
         * @return An object instance containing the response data.
         * @throws InternalServiceException If the message could not be retrieved or converted to the response class.
         */
        @Override
        public T sync(HttpMethod httpMethod) {
            T responseObject;
            try {
                SyncRequest syncRequest = new SyncRequest(restTemplate, httpMethod, url, headers, parameters, requestObject);

                String responseString = syncRequest.getResponse();

                if (responseClass == String.class) {
                    //unchecked - This is fine, we know it wants a string.
                    //noinspection unchecked
                    responseObject = (T)responseString;
                } else {
                    responseObject = MappingUtils.convertFromJson(responseString, responseClass);
                }
            } catch (IOException | HttpRequestException e) {
                throw new InternalServiceException(InternalServiceException.Reason.EXTERNAL, e);
            }

            return responseObject;
        }
    }

    /**
     * Performs the logic of making the Synchronous request.
     * <p>Separated for re-usability with non rest based requests.</p>
     */
    @Data
    public static class SyncRequest {

        private final String response;

        /**
         * Performs a synchronous http request, returning the raw string response body.
         * <p>The response message is saved as a String in the response class property, accessed via getter.</p>
         *
         * @param restTemplate Spring's RestTemplate client to use to make the request
         * @param httpMethod The request method type of the request
         * @param url The url of the http request
         * @param headers The headers for the http request
         * @param parameters The parameters for the http request
         * @param body The body object for the http request
         */
        SyncRequest(RestTemplate restTemplate, HttpMethod httpMethod, String url, Map<String, String> headers, Map<String, String> parameters, Object body) {
            org.springframework.http.HttpMethod springHttpMethod = org.springframework.http.HttpMethod.valueOf(httpMethod.toString());
            MultiValueMap<String, String> mutliValueHeaders = new LinkedMultiValueMap<>(headers.entrySet().stream()
                    .map(h -> {
                        Map<String, String> entry = new HashMap<>();
                        entry.put("header", h.getKey());
                        entry.put("value", h.getValue());
                        return entry;
                    }).collect(Collectors.toMap(
                            m -> m.get("header"),
                            m -> Collections.singletonList(m.get("value"))
                    )));

            HttpEntity<Object> httpEntity = new HttpEntity<>(body, mutliValueHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, springHttpMethod, httpEntity, String.class, parameters);
            response = responseEntity.getBody();
        }
    }

}
