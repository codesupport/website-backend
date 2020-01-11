package dev.codesupport.web.common.service.service;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import dev.codesupport.web.common.util.MappingUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectToUrlEncodedConverter implements HttpMessageConverter<Object> {
    private static final String ENCODING = "UTF-8";

    @Override
    public boolean canRead(Class clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class clazz, MediaType mediaType) {
        return getSupportedMediaTypes().contains(mediaType);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED);
    }

    @Override
    public Object read(Class clazz, HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        throw new NotImplementedException("Read not implemented");
    }

    @Override
    public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage) throws HttpMessageNotWritableException {
        String body = MappingUtils
                .convertToType(o, UrlEncodedWriter.class)
                .toString();

        try {
            outputMessage.getBody().write(body.getBytes(ENCODING));
        } catch (IOException e) {
            // Well... shit.
        }
    }

    private static class UrlEncodedWriter {
        private final Map<String, Object> properties = new HashMap<>();

        @JsonAnySetter
        public void set(String name, Object value) {
            properties.put(name, value);
        }

        @Override
        public String toString() {
            List<String> urlProperties = new ArrayList<>();
            try {
                for (Map.Entry<String, Object> entry : properties.entrySet()) {
                    String name = URLEncoder.encode(entry.getKey(), ENCODING);
                    String value;
                    if (entry.getValue() != null) {
                        value = URLEncoder.encode(entry.getValue().toString(), ENCODING);
                    } else {
                        value = "";
                    }

                    urlProperties.add(name + "=" + value);
                }
            } catch (UnsupportedEncodingException e) {
                //TODO: fix this
            }
            return String.join("&", urlProperties);
        }
    }
}