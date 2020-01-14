package dev.codesupport.web.common.service.http;

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

@Component
@EqualsAndHashCode
public class SpringHttpClient implements HttpClient {

    RestTemplate restTemplate;

    @Autowired
    public SpringHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public <S extends Serializable, T extends Serializable> RestRequest<S, T> rest(Class<S> requestClass, Class<T> responseClass) {
        return new SpringRestRequest<>(restTemplate, requestClass, responseClass);
    }

    @EqualsAndHashCode(callSuper = true)
    @ToString
    public static class SpringRestRequest<S extends Serializable, T extends Serializable> extends RestRequest<S, T> {

        @ToString.Exclude
        private RestTemplate restTemplate;

        SpringRestRequest(RestTemplate restTemplate, Class<S> requestClass, Class<T> responseClass) {
            super(requestClass, responseClass);
            this.restTemplate = restTemplate;
        }

        @Override
        public T sync(HttpMethod httpMethod) {
            T responseObject;
            org.springframework.http.HttpMethod springHttpMethod = org.springframework.http.HttpMethod.valueOf(httpMethod.toString());
            try {
                SyncRequest syncRequest = new SyncRequest(restTemplate, springHttpMethod, url, headers, parameters, requestObject);

                String responseString = syncRequest.getResponse();

                responseObject = MappingUtils.convertFromJson(responseString, responseClass);
            } catch (IOException | HttpRequestException e) {
                throw new InternalServiceException(InternalServiceException.Reason.EXTERNAL, e);
            }

            return responseObject;
        }
    }

    @Data
    public static class SyncRequest {

        private final String response;

        SyncRequest(RestTemplate restTemplate, org.springframework.http.HttpMethod httpMethod, String url, Map<String, String> headers, Map<String, String> parameters, Object body) {
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
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class, parameters);
            response = responseEntity.getBody();
        }
    }

}
